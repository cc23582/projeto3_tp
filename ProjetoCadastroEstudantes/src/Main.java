import java.io.*;
import java.util.Scanner;

public class Main
{
    static ManterEstudantes estud;
    static Scanner teclado;
    static String[] siglas = new String[15];

    public static void main(String[] args) throws Exception {
        estud = new ManterEstudantes(15);
        teclado = new Scanner(System.in);
        lerSiglas(); // lê o arquivo de siglas
        estud.lerDados("estudantes.txt");
        estud.ordenar();
        seletorDeOpcoes();
        estud.gravarDados("estudantes.txt");
    }

    public static void lerSiglas() throws Exception {
        BufferedReader arq = new BufferedReader(new FileReader("siglasDisc.txt"));
        for (int i = 0; i < 15; i++) {
            siglas[i] = arq.readLine();
        }
        arq.close();
    }

    public static void seletorDeOpcoes() throws Exception {
        int opcao = -1;
        do {
            System.out.println("\nOperações disponíveis:");
            System.out.println("0 - Terminar programa");
            System.out.println("1 - Incluir estudantes");
            System.out.println("2 - Excluir estudantes");
            System.out.println("3 - Exibir estudante");
            System.out.println("4 - Listar estudantes");
            System.out.println("5 - Alterar estudantes");
            System.out.println("6 - Ir ao início");
            System.out.println("7 - Ir ao próximo");
            System.out.println("8 - Ir ao anterior");
            System.out.println("9 - Ir ao último");
            System.out.println("10 - Estatísticas");
            System.out.print("\nDigite o número da operação desejada: ");
            opcao = teclado.nextInt();
            teclado.nextLine();

            switch (opcao)
            {
                case 1: inclusao(); break;
                case 2: exclusao(); break;
                case 3: exibicao(); break;
                case 4: listagem(); break;
                case 5: alteracao(); break;
                case 6: irAoInicio(); break;
                case 7: irAoProximo(); break;
                case 8: irAoAnterior(); break;
                case 9: irAoUltimo(); break;
                case 10: fazEstatisticas(); break;
            }
        } while (opcao != 0);
    }

    public static void inclusao() throws Exception {
        while (true) {
            System.out.print("RA do estudante (0 para terminar): ");
            int raDigitado = teclado.nextInt();
            teclado.nextLine();
            if (raDigitado == 0) break;

            String ra = String.format("%05d", raDigitado);
            Estudante proc = new Estudante(ra);
            if (estud.existe(proc)) {
                System.out.println("\nRA repetido!");
            } else {
                System.out.print("Código do curso: ");
                int cursoLido = teclado.nextInt();
                teclado.nextLine();
                String curso = String.format("%02d", cursoLido);
                System.out.print("Nome do estudante: ");
                String nome = teclado.nextLine();
                estud.incluirEm(new Estudante(curso, ra, nome), estud.getOnde());
                System.out.println("\nEstudante incluído.");
            }
        }
    }

    public static void exclusao() throws Exception {
        while (true) {
            System.out.print("RA do estudante (0 para terminar): ");
            int raDigitado = teclado.nextInt();
            teclado.nextLine();
            if (raDigitado == 0) break;

            String ra = String.format("%05d", raDigitado);
            Estudante proc = new Estudante(ra);
            if (!estud.existe(proc))
                System.out.println("\nRA não encontrado!");
            else {
                estud.excluir(estud.getOnde());
                System.out.println("Estudante excluído.");
            }
        }
    }

    public static void exibicao() throws Exception {
        while (true) {
            System.out.print("RA do estudante (0 para terminar): ");
            int raDigitado = teclado.nextInt();
            teclado.nextLine();
            if (raDigitado == 0) break;

            String ra = String.format("%05d", raDigitado);
            Estudante proc = new Estudante(ra);
            if (!estud.existe(proc))
                System.out.println("\nRA não encontrado!");
            else {
                System.out.println(cabecalhoNotas());
                System.out.println(estud.valorDe(estud.getOnde()));
            }
        }
    }

    public static void listagem() throws Exception {
        System.out.println("\nRelação de estudantes cadastrados:");
        System.out.println(cabecalhoNotas());
        for (int i = 0; i < estud.getTamanho(); i++)
            System.out.println(estud.valorDe(i));
        System.out.println();
    }

    public static String cabecalhoNotas() {
        String linha = "Curso  RA    Nome                           Qn ";
        for (int i = 0; i < 15; i++)
            linha += String.format("%-6s", siglas[i]);
        return linha;
    }

    public static void alteracao() throws Exception {
        while (true) {
            System.out.print("RA do estudante (0 para terminar): ");
            int raDigitado = teclado.nextInt();
            teclado.nextLine();
            if (raDigitado == 0) break;

            String ra = String.format("%05d", raDigitado);
            Estudante proc = new Estudante(ra);

            if (!estud.existe(proc)) {
                System.out.println("\nNão existe um estudante com este RA!");
            } else {
                Estudante atual = estud.valorDe(estud.getOnde());
                System.out.println("Dados atuais:");
                System.out.println(atual);

                System.out.print("Novo código do curso ([Enter] para manter): ");
                String novoCurso = teclado.nextLine();
                if (!novoCurso.equals("")) atual.setCurso(novoCurso);

                System.out.print("Novo nome do estudante ([Enter] para manter): ");
                String novoNome = teclado.nextLine();
                if (!novoNome.equals("")) atual.setNome(novoNome);

                estud.alterar(atual, estud.getOnde());
                System.out.println("Estudante atualizado!");
            }
        }
    }

    public static void irAoInicio() {
        estud.irAoInicio();
        System.out.println(estud.valorDe(estud.getPosicaoAtual()));
    }

    public static void irAoProximo() {
        if (!estud.estaNoFim()) {
            estud.irAoProximo();
            System.out.println(estud.valorDe(estud.getPosicaoAtual()));
        } else System.out.println("Já está no último registro.");
    }

    public static void irAoAnterior() {
        if (!estud.estaNoInicio()) {
            estud.irAoAnterior();
            System.out.println(estud.valorDe(estud.getPosicaoAtual()));
        } else System.out.println("Já está no primeiro registro.");
    }

    public static void irAoUltimo() {
        estud.irAoFim();
        System.out.println(estud.valorDe(estud.getPosicaoAtual()));
    }

    public static void fazEstatisticas() throws Exception {
        int qtdEstudantes = estud.getTamanho();
        if (qtdEstudantes == 0) {
            System.out.println("Nenhum estudante cadastrado!");
            return;
        }

        double[] somaNotas = new double[15];
        int[] contNotas = new int[15];
        int[] aprovados = new int[15];
        int[] retidos = new int[15];

        Estudante melhorAluno = null;
        double maiorMedia = -1;

        for (int i = 0; i < qtdEstudantes; i++) {
            Estudante e = estud.valorDe(i);
            double[] notas = e.getNotas();
            int qn = e.getQuantasNotas();
            double soma = 0;
            for (int j = 0; j < qn; j++) {
                soma += notas[j];
                somaNotas[j] += notas[j];
                contNotas[j]++;
                if (notas[j] >= 6.0) aprovados[j]++;
                else retidos[j]++;
            }
            double mediaAluno = soma / qn;
            if (mediaAluno > maiorMedia) {
                maiorMedia = mediaAluno;
                melhorAluno = e;
            }
        }

        double[] mediasDisc = new double[15];
        for (int j = 0; j < 15; j++)
            if (contNotas[j] > 0)
                mediasDisc[j] = somaNotas[j] / contNotas[j];

        int discMaisAprov = 0, discMaisRetidos = 0;
        for (int j = 1; j < 15; j++) {
            if (aprovados[j] > aprovados[discMaisAprov]) discMaisAprov = j;
            if (retidos[j] > retidos[discMaisRetidos]) discMaisRetidos = j;
        }

        int discMaiorMedia = 0, discMenorMedia = 0;
        for (int j = 1; j < 15; j++) {
            if (mediasDisc[j] > mediasDisc[discMaiorMedia]) discMaiorMedia = j;
            if (mediasDisc[j] < mediasDisc[discMenorMedia]) discMenorMedia = j;
        }

        System.out.println("\n===== Estatísticas =====");
        System.out.println("Disciplina com mais aprovações: " + siglas[discMaisAprov]);
        System.out.println("Disciplina com mais retenções: " + siglas[discMaisRetidos]);
        System.out.println("Aluno com maior média: " + melhorAluno.getNome().trim() +
                " (" + melhorAluno.getRa().trim() + ") com média " +
                String.format("%.2f", maiorMedia));

        double[] notasMelhor = melhorAluno.getNotas();
        int qn = melhorAluno.getQuantasNotas();
        int maiorNotaDisc = 0, menorNotaDisc = 0;
        for (int j = 1; j < qn; j++) {
            if (notasMelhor[j] > notasMelhor[maiorNotaDisc]) maiorNotaDisc = j;
            if (notasMelhor[j] < notasMelhor[menorNotaDisc]) menorNotaDisc = j;
        }
        System.out.println("Maior nota do melhor aluno: " + siglas[maiorNotaDisc]);
        System.out.println("Menor nota do melhor aluno: " + siglas[menorNotaDisc]);

        System.out.println("\nMédias por disciplina:");
        for (int j = 0; j < 15; j++)
            if (contNotas[j] > 0)
                System.out.printf("%-7s: %.2f\n", siglas[j], mediasDisc[j]);

        double maiorNotaNaMenor = -1;
        Estudante alunoMaiorNaMenor = null;
        for (int i = 0; i < qtdEstudantes; i++) {
            Estudante e = estud.valorDe(i);
            double[] notas = e.getNotas();
            if (e.getQuantasNotas() > discMenorMedia &&
                    notas[discMenorMedia] > maiorNotaNaMenor) {
                maiorNotaNaMenor = notas[discMenorMedia];
                alunoMaiorNaMenor = e;
            }
        }

        double menorNotaNaMaior = 11;
        Estudante alunoMenorNaMaior = null;
        for (int i = 0; i < qtdEstudantes; i++) {
            Estudante e = estud.valorDe(i);
            double[] notas = e.getNotas();
            if (e.getQuantasNotas() > discMaiorMedia &&
                    notas[discMaiorMedia] < menorNotaNaMaior) {
                menorNotaNaMaior = notas[discMaiorMedia];
                alunoMenorNaMaior = e;
            }
        }

        System.out.println("\nNa disciplina com MENOR média (" + siglas[discMenorMedia] +
                "), o aluno com MAIOR nota foi: " + alunoMaiorNaMenor.getNome().trim() +
                " (" + maiorNotaNaMenor + ")");
        System.out.println("Na disciplina com MAIOR média (" + siglas[discMaiorMedia] +
                "), o aluno com MENOR nota foi: " + alunoMenorNaMaior.getNome().trim() +
                " (" + menorNotaNaMaior + ")");
    }
}
