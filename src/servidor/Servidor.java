package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giovanne
 */
public class Servidor extends Thread{

    private ServerSocket socketServer;
    public String arquivoCliente;
    public String arquivoResposta;
    public volatile boolean status = false;
    
    public void setStatus(boolean status){
        this.status = status;
    }
    
    public boolean getStatus(){
        return status;
    }
    
    private File[] achaArquivos(String diretorio) {
        File dir = new File(diretorio);
        File fList[] = dir.listFiles();
        for (int i = 0; i < fList.length; i++) {
            System.out.println(fList[i]);
        }
        return fList;
    }
    
    @Override
    public void run(){
        this.status = true;
        System.out.println("Iniciando thread");
        try {
            File arquivosEncontrados [];
            
            while (status) {
                if(socketServer == null || !socketServer.isBound())
                    socketServer = new ServerSocket(6789);
                System.out.println("Server ON");
                Socket conexao = socketServer.accept();
                System.out.println("Status: "+status);
                BufferedReader doCliente = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                DataOutputStream paraCliente = new DataOutputStream(conexao.getOutputStream());
                arquivoCliente = doCliente.readLine();
                System.out.println("Comando recebido "+arquivoCliente);
                arquivoCliente = arquivoCliente + " visto pelo servidor";
//                paraCliente.writeBytes(achaArquivos(arquivoCliente).toString());
                paraCliente.writeBytes(arquivoCliente);
                System.out.println("Requisição enviada!");
                paraCliente.close();
                conexao.close();
                if(!status)
                    return;
            }
            
        } catch (IOException ex) {
            System.out.println("Socket do servidor fechado enquanto esperava cliente!!");
        }
    }
    
    public void kill(){
        this.status = false;
        try {
            socketServer.close();
        } catch (IOException ex) {
            System.out.println("Socket do servidor fechado!!");
        }
    }
}
