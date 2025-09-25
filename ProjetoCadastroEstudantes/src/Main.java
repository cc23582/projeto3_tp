import java.io.*;
import java.util.Scanner;

public class Main
{
    static ManterEstudantes estud;
    static Scanner teclado;

    public static void main(String[] args) throws Exception {
        estud = new ManterEstudantes(15);
        teclado = new Scanner(System.in);   // System.in representa o teclado
        estud.lerDados("estudantes.txt");
        seletorDeOpcoes();
        estud.gravarDados("estudantes.txt");
    }

    public static void seletorDeOpcoes() throws Exception {
        int opcao = -1;
        do {
            System.out.println("\nOPerações disponíveis:");
            System.out.println("0 - Terminar programa");
            System.out.println("1 - Incluir estudantes");
            System.out.println("2 - Excluir estudantes");
            System.out.println("3 - Exibir estudantes");
            System.out.println("4 - Listar estudantes");
            System.out.println("5 - Ir ao início");
            System.out.println("6 - Ir ao proximo");
            System.out.println("7 - Ir ao anterior");
            System.out.println("8 - Ir ao último");
            System.out.println("9 - Estatísticas");
            System.out.println("\nDigite o número da operação desejada:");
            opcao = teclado.nextInt();  // lê um inteiro pelo teclado

            // definir e tratar a operação escolhida pelo usuário:
            switch (opcao)
            {
                case 1 : inclusao(); break;
                case 2 : exclusao(); break;
                case 3 : exibicao(); break;
                case 4 : listagem();
            }
        }
        while (opcao != 0);
    }

    public static void inclusao() throws Exception {
        String curso, ra = "99999", nome;
        while (ra != "00000")
        {
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            if (raDigitado != 0)
            {
                ra = String.format("%05d", raDigitado);
                // não podemos ter ra repetido, temos de pesquisá-lo no vetor
                // antes de prosseguir a inclusão:
                // criamos um objeto Estudante para poder chamar o método existe()
                Estudante proc = new Estudante(ra);
                if (estud.existe(proc))     // ajusta valor de ondeEsta
                    System.out.println("\nRA repetido!");
                else  // ra não repetido, lemos os demais dados
                {
                    System.out.print("Código do curso: ");
                    int cursoLido = teclado.nextInt();
                    curso = String.format("%02d", cursoLido); // "00" a "99"
                    System.out.print("Nome do estudante: ");
                    nome = teclado.next();
                    // inclui em ordem crescente de ra, usando o valor de ondeEsta
                    // como índice de inclusão do novo estudante
                    estud.incluirEm(new Estudante(curso, ra, nome), estud.getOnde());
                    System.out.println("\nEstudante incluído.");
                }
            }
        }
    }

    public static void exclusao() throws Exception
    {
        String ra = "99999";
        while (ra != "00000") {
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            if (raDigitado != 0) {
                ra = String.format("%05d", raDigitado);
                // temos de pesquisar esse ra no vetor para descobrirmos
                // em que índice ele está para podermos excluir desse índice
                // criamos um objeto Estudante para poder chamar o método existe()
                Estudante proc = new Estudante(ra);
                if (!estud.existe(proc))     // ajusta valor de ondeEsta
                    System.out.println("\nRA não encontrado!");
                else  // ra não repetido, lemos os demais dados
                {
                    estud.excluir(estud.getOnde());
                    System.out.println("Estudante excluído.");
                }
            }
        }
    }

    public static void exibicao() throws Exception
    {
        String ra = "99999";
        while (ra != "00000") {
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            if (raDigitado != 0) {
                ra = String.format("%05d", raDigitado);
                // temos de pesquisar esse ra no vetor para descobrirmos
                // em que índice ele está para podermos excluir desse índice
                // criamos um objeto Estudante para poder chamar o método existe()
                Estudante proc = new Estudante(ra);
                if (!estud.existe(proc))     // ajusta valor de ondeEsta
                    System.out.println("\nRA não encontrado!");
                else  // ra não repetido, lemos os demais dados
                {
                    System.out.println(estud.valorDe(estud.getOnde()));
                }
            }
        }
    }

    public static void listagem() throws Exception
    {
        System.out.println("\nRelação de estudantes cadastrados:");
        System.out.println("Curso  RA    Nome                            Notas");
        for (int indice=0; indice < estud.getTamanho(); indice++)
            System.out.println(estud.valorDe(indice));
        System.out.println();
    }

}