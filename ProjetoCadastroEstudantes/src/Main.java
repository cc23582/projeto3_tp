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
        estud.lerDados("src/estudantes.txt");
        estud.ordenar();
        seletorDeOpcoes();
        estud.gravarDados("src/estudantes.txt");
    }

    public static void lerSiglas() throws Exception {
        BufferedReader arq = new BufferedReader(new FileReader("src/siglasDisc.txt"));
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

    //Vamos ter que incluir as notas nesse método, pq se não o estudante fica sem notas o que faz dar erro no fazEstatistica
    public static void inclusao() throws Exception {
        String curso, ra = "99999", nome;
        while (!ra.equals("00000")) { //mudamos para equals
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            teclado.nextLine(); // limpa o \n pendente

            if (raDigitado != 0) {
                ra = String.format("%05d", raDigitado);

                // não podemos ter ra repetido, temos de pesquisá-lo no vetor
                // antes de prosseguir a inclusão:
                // criamos um objeto Estudante para poder chamar o método existe()
                Estudante proc = new Estudante(ra);
                if (estud.existe(proc)) {       // ajusta valor de ondeEsta
                    System.out.println("\nRA repetido!");
                } else {        // ra não repetido, lemos os demais dados
                    System.out.print("Código do curso: ");
                    int cursoLido = teclado.nextInt();
                    teclado.nextLine();
                    curso = String.format("%02d", cursoLido);
                    System.out.print("Nome do estudante: ");
                    nome = teclado.nextLine();
                    // inclui em ordem crescente de ra, usando o valor de ondeEsta
                    // como índice de inclusão do novo estudante
                    Estudante novo = new Estudante(curso, ra, nome);

                    // === inclusão das notas ===
                    double[] notas = new double[15];
                    int qn = 0;
                    System.out.println("Digite as notas (0 a 10). Enter vazio para parar:");
                    for (int i = 0; i < siglas.length; i++) {
                        System.out.print(siglas[i] + ": ");
                        String entrada = teclado.nextLine().trim();
                        if (entrada.equals("")) break; // para antes das 15
                        double nota = Double.parseDouble(entrada);
                        novo.incluirNota(nota);
                        qn++;
                    }
                    novo.setQuantasNotas(qn);
                    estud.incluirEm(novo, estud.getOnde());
                    System.out.println("\nEstudante incluído.");
                }
            } else
            {
                ra = "00000";
            }
        }
    }


    public static void exclusao() throws Exception
    {
        String ra = "99999";
        while (!ra.equals("00000")) { //mudamos para equals
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            ra = String.format("%05d", raDigitado);

            if (raDigitado != 0) {
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

    public static void exibicao() throws Exception {
        String ra = "99999";
        while (!ra.equals("00000")) {
            System.out.print("RA do estudante: (0) para terminar: ");
            int raDigitado = teclado.nextInt();
            if (raDigitado != 0) {
                ra = String.format("%05d", raDigitado);
                Estudante proc = new Estudante(ra);
                if (!estud.existe(proc)) {
                    System.out.println("\nRA não encontrado!");
                } else {

                    System.out.println(cabecalhoNotas());
                    System.out.println(estud.valorDe(estud.getOnde()));
                }
            } else {
                ra = "00000"; // encerra o laço
            }
        }
    }

    public static void listagem() throws Exception {
        System.out.println("\nRelação de estudantes cadastrados:");
        System.out.println(cabecalhoNotas());
        for (int indice = 0; indice < estud.getTamanho(); indice++) {
            System.out.println(estud.valorDe(indice));
        }
        System.out.println();
    }

    public static String cabecalhoNotas() {
        String linha = "Curso RA     Nome                             Qn ";
        for (int i = 0; i < 15; i++)
            linha += String.format("%-6s ", siglas[i]);
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

            // === alterar notas =====
            System.out.println("Deseja alterar as notas? (s/n)");
            String resp = teclado.nextLine().trim().toLowerCase();
            if (resp.equals("s")) {
                double[] notas = atual.getNotas();
                int qn = atual.getQuantasNotas();

                for (int i = 0; i < siglas.length; i++) {
                    if (i < qn)
                        System.out.print(siglas[i] + " (atual: " + notas[i] + ") [Enter p/ manter]: ");
                    else
                        System.out.print(siglas[i] + " (sem nota) [Enter p/ parar]: ");
                    String entrada = teclado.nextLine().trim();
                    if (entrada.equals("")) {
                        if (i >= qn) break; // parar se não havia nota
                        continue; // mantém se já existia
                    }
                    double novaNota = Double.parseDouble(entrada);
                    if (i < qn)
                        notas[i] = novaNota;
                    else {
                        atual.incluirNota(novaNota);
                        qn++;
                    }
                }
                atual.setQuantasNotas(qn);
            }
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

        System.out.println("\n===== Estatísticas =====");
        double[] mediasDisc = calculaMediasPorDisciplina();
        int[] aprovados = contaAprovados();
        int[] retidos = contaRetidos();
        int discMaisAprov = indiceMaiorValor(aprovados);
        int discMaisRetidos = indiceMaiorValor(retidos);
        int discMaiorMedia = indiceMaiorValor(mediasDisc);
        int discMenorMedia = indiceMenorValor(mediasDisc);

        System.out.println("Disciplina com MAIS aprovações: " + siglas[discMaisAprov]);
        System.out.println("Disciplina com MAIS retenções: " + siglas[discMaisRetidos]);

        Estudante melhorAluno = alunoComMaiorMedia();
        System.out.println("Aluno com MAIOR média: " + melhorAluno.getNome().trim() +
                " (" + melhorAluno.getRa().trim() + ") com média " +
                String.format("%.2f", melhorAluno.mediaDasNotas()));
        mostraMaiorEMenorNotaDoMelhorAluno(melhorAluno);
        System.out.println("\nMédias por disciplina:");
        for (int j = 0; j < siglas.length; j++)
            System.out.printf("%-7s: %.2f\n", siglas[j], mediasDisc[j]);

        mostraDestaquesNasDisciplinasExtremas(discMenorMedia, discMaiorMedia);
    }


//======================= Métodos para o fazEstatisticas()========

    // Calcula a média aritmética das disciplinas
    private static double[] calculaMediasPorDisciplina() throws Exception {
        double[] soma = new double[15];
        int[] cont = new int[15];
        for (int i = 0; i < estud.getTamanho(); i++) {
            Estudante e = estud.valorDe(i);
            double[] notas = e.getNotas();
            for (int j = 0; j < e.getQuantasNotas(); j++) {
                soma[j] += notas[j];
                cont[j]++;
            }
        }
        double[] medias = new double[15];
        for (int j = 0; j < 15; j++)
            medias[j] = cont[j] > 0 ? soma[j] / cont[j] : 0;
        return medias;
    }

    // conta os alunos aprovados
    private static int[] contaAprovados() throws Exception {
        int[] aprovados = new int[15];
        for (int i = 0; i < estud.getTamanho(); i++) {
            Estudante e = estud.valorDe(i);
            for (int j = 0; j < e.getQuantasNotas(); j++)
                if (e.getNotas()[j] >= 6) aprovados[j]++;
        }
        return aprovados;
    }

    // conta quantos alunos foram retidos
    private static int[] contaRetidos() throws Exception {
        int[] retidos = new int[15];
        for (int i = 0; i < estud.getTamanho(); i++) {
            Estudante e = estud.valorDe(i);
            for (int j = 0; j < e.getQuantasNotas(); j++)
                if (e.getNotas()[j] < 6) retidos[j]++;
        }
        return retidos;
    }

    // Retorna í do maior valor em vetor de double
    private static int indiceMaiorValor(double[] v) {
        int indice = 0;
        for (int i = 1; i < v.length; i++)
            if (v[i] > v[indice]) indice = i;
        return indice;
    }

    // Retorna í do menor valor em vetor de double
    private static int indiceMenorValor(double[] v) {
        int indice = 0;
        for (int i = 1; i < v.length; i++)
            if (v[i] < v[indice] && v[i] != 0) indice = i;
        return indice;
    }

    // Retorna í do maior valor em vetor de int
    private static int indiceMaiorValor(int[] v) {
        int indice = 0;
        for (int i = 1; i < v.length; i++)
            if (v[i] > v[indice] && v[i] != 0) indice = i;
        return indice;
    }

    // retorna o aluno com a maior média geral
    private static Estudante alunoComMaiorMedia() throws Exception {
        Estudante melhor = null;
        double maior = -1;
        for (int i = 0; i < estud.getTamanho(); i++) {
            Estudante e = estud.valorDe(i);
            double media = e.mediaDasNotas();
            if (media > maior) {
                maior = media;
                melhor = e;
            }
        }
        return melhor;
    }

    // Exibe as diciplina de maior e menor nota do melhor aluno
    private static void mostraMaiorEMenorNotaDoMelhorAluno(Estudante e) throws Exception {
        double[] notas = e.getNotas();
        int qn = e.getQuantasNotas();
        int maior = 0, menor = 0;

        for (int j = 1; j < qn; j++) {
            if (notas[j] > notas[maior]) maior = j;
            if (notas[j] < notas[menor]) menor = j;
        }

        System.out.println("Maior nota do melhor aluno: " + siglas[maior]);
        System.out.println("Menor nota do melhor aluno: " + siglas[menor]);
    }

    // Mostra o pior e melhor aluno
    private static void mostraDestaquesNasDisciplinasExtremas(int discMenorMedia, int discMaiorMedia) throws Exception {
        double maiorNotaNaMenor = -1, menorNotaNaMaior = 11;
        Estudante alunoMaiorNaMenor = null, alunoMenorNaMaior = null;
        for (int i = 0; i < estud.getTamanho(); i++) {
            Estudante e = estud.valorDe(i);
            double[] notas = e.getNotas();



            if (e.getQuantasNotas() > discMenorMedia) {
                if (notas[discMenorMedia] > maiorNotaNaMenor) {
                    maiorNotaNaMenor = notas[discMenorMedia];
                    alunoMaiorNaMenor = e;
                }
            }
            if (e.getQuantasNotas() > discMaiorMedia) {
                if (notas[discMaiorMedia] < menorNotaNaMaior) {
                    menorNotaNaMaior = notas[discMaiorMedia];
                    alunoMenorNaMaior = e;
                }
            }
        }

        if (alunoMenorNaMaior != null) {
            System.out.println("\nNa disciplina com MENOR média (" + siglas[discMenorMedia] +
                    "), o aluno com MAIOR nota foi: " + alunoMaiorNaMenor.getNome().trim() +
                    " (" + String.format("%.1f", maiorNotaNaMenor) + ")");
        }
        if (alunoMaiorNaMenor != null) {
            System.out.println("Na disciplina com MAIOR média (" + siglas[discMaiorMedia] +
                    "), o aluno com MENOR nota foi: " + alunoMenorNaMaior.getNome().trim() +
                    " (" + String.format("%.1f", menorNotaNaMaior) + ")");
        }
    }

}
