package JAVA.questões;


/*
    Protocolo TCP/IP 
    O Protocolo TCP/IP (Transmission Control Protocol - Protocolo
    de Controle de Transmissão / Internet Protocol - Protocolo de 
    Internet) é um conjunto de protocolos de comunicação em uma 
    rede de computadores para transmissão de pacotes na rede. O TCP
    é um mecanismo de transporte que confere confiança na transmissão
    de dados desde o computador ou dispositivo (com acesso à rede) 
    ao host de origem, confirmando que o pacote foi recebido com 
    sucesso. A transferência é feita dividindo os dados em vários 
    pacotes e anexando um cabeçalho com outros parâmetros importantes
    do protocolo TCP e que, como os pacotes podem ser enviados por 
    caminhos diferentes, eles podem chegar ao host de destino sem ordem.
    No host de destino, os pacotes são remontados de forma que a aplicação
    seja visualizada organizada e sequencialmente.

    Suponha que você está criando o monitor do TCP e desenvolva o algoritmo
    que, a partir da chegada dos pacotes desviados, apresente no monitor do 
    host de destino os mesmos pacotes, porém de forma ordenada. Assuma que 
    seja enviado o bit "1" para iniciar a transferência e o bit "0" para finalizar 
    a transferência dos pacotes.
*/




import java.util.*;

public class TCP {
    static class Packet implements Comparable<Packet> {
        int num;
        String line;
        Packet(int n) {
            num = n;
            line = String.format("Package %03d", n);
        }
        public int compareTo(Packet p) {
            return this.num - p.num;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Digite 1 para novo caso, 0 para sair: ");
            String s = sc.next();
            if (s.equals("0")) break;

            ArrayList<Packet> list = new ArrayList<>();

            System.out.println("Digite pacotes ('Package XXX') ou 0 para terminar:");
            while (true) {
                String w = sc.next();
                if (w.equals("0")) break;

                int num = sc.nextInt();
                list.add(new Packet(num));
            }

            Collections.sort(list);

            System.out.println("\nPacotes ordenados:");
            for (Packet p : list)
                System.out.println(p.line);

            System.out.println();
        }
    }
}
