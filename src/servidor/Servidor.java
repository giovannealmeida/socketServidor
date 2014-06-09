package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Giovanne
 */
public class Servidor extends Thread {

    private ServerSocket socketServer;
    OutputStream socketOut = null;
    ServerSocket servsock = null;
    public String arquivoCliente;
    public String arquivoResposta;
    public volatile boolean status = false;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public void run() {
        this.status = true;
        System.out.println("Iniciando thread");
        try {
            FileInputStream fileIn = null;

            File arquivosEncontrados[];

            while (status) {
                if (socketServer == null || !socketServer.isBound()) {
                    socketServer = new ServerSocket(6789);
                    System.out.println("Servidor esperando na porta 6789...");
                }

                Socket conexao = socketServer.accept();
                socketOut = conexao.getOutputStream();
                BufferedReader doCliente = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                arquivoCliente = doCliente.readLine();

                byte[] flagBuffer = new byte[1];
                byte[] cbuffer = new byte[1024];
                int bytesRead;
                
                File file = new File(arquivoCliente);
                System.out.println("Arquivo a ser buscado: " + file);
                try {
                    fileIn = new FileInputStream(file);
                    System.out.println("Input stream: " + fileIn);
                } catch (FileNotFoundException ex) {
                    //Escrevedo a flag de falha -- erro no arquivo
                    socketOut.write(flagBuffer, 0, 0);
                    conexao.close();
                    socketOut.close();
                    continue;
                }
                
                //Escrevedo a flag de sucesso -- arquivo encontrado
                socketOut.write(flagBuffer, 0, 1);
                //Escrevedo o tamanho do arquivo
                byte[] tamanhoBuffer = new byte[1024];
                
                new DataOutputStream(socketOut).writeLong(file.length());
                
                int i = 0;
                while ((bytesRead = fileIn.read(cbuffer)) != -1) {
                    socketOut.write(cbuffer, 0, bytesRead);
                    socketOut.flush();
                }
                System.out.println("Arquivo enviado!");
                conexao.close();
                socketOut.close();
            }
        } catch (IOException ex) {
            System.out.println("Socket do servidor fechado enquanto esperava cliente!!");
        }
    }

    public void kill() {
        this.status = false;
        try {
            socketServer.close();
        } catch (IOException ex) {
            System.out.println("Socket do servidor fechado!!");
        }
    }
}
