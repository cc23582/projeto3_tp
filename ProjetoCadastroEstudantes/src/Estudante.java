import java.io.*;
import java.util.Locale;

public class Estudante
{
    @Override
    public String toString() {
        String saida = " " + curso + "   " + ra + "  " + nome + "    " + quantasNotas+" ";
        for (int indice=0; indice < quantasNotas; indice++)
            saida += String.format("%4.1f ",notas[indice]);
        return saida;
    }

    private String curso;
    private String ra;
    private String nome;
    private int quantasNotas;   // tamanho lógico do vetor abaixo
    private double[] notas;     // vetor com as notas do estudante

    public Estudante()
    {
        // construtor sem parâmetros
        // limpa todos os atributos com cadeia vazia, null e zeros,
        // de acordo com o tipo de cada atributo
    }

    public Estudante(String c, String r, String n) throws Exception {
        setCurso(c);
        setRa(r);
        setNome(n);
        setNotas(new double[15]);       // 15 notas no máximo
        setQuantasNotas(0); // número de notas ao instanciar
    }

    public Estudante(String raLido) throws Exception
    {
        this("00", raLido, " ");
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) throws Exception
    {
        if (curso.length() != 2)
            throw new Exception("Curso em formato inválido!");

        this.curso = curso;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) throws Exception
    {
        if (ra.equals("") || ra.length() > 5)
            throw new Exception("RA em formato inválido!");

        this.ra = String.format("%5s", ra);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws Exception
    {
        if (nome.equals("") || nome.length() > 30)
            throw new Exception("Nome em formato inválido!");

        this.nome = String.format("%-30s", nome);
    }

    public int getQuantasNotas() {
        return quantasNotas;
    }

    public void setQuantasNotas(int quantasNotas) throws Exception
    {
        if (quantasNotas < 0 || quantasNotas > 15)
            throw new Exception("Quantidade de notas com valor inválido!");

        this.quantasNotas = quantasNotas;
    }

    public double[] getNotas() {
        return notas;
    }

    public void setNotas(double[] notas) throws Exception
    {
        if (notas.length > 15)
            throw new Exception("Há mais notas do que o máximo permitido!");

        for (int indNota = 0; indNota < quantasNotas; indNota++)
            if (notas[indNota] < 0 || notas[indNota] > 10)
               throw new
                  Exception(String.format("%2da. nota inválida!", indNota+1));
        this.notas = notas;
    }

    // throws Exception informa que poderá ocorrer um erro na leitura de arquivo
    public boolean leuLinhaDoArquivo(BufferedReader arq) throws Exception {
        if (arq != null)    // se arquivo de entrada está aberto (pela aplicação)
        {
            String linhaDoArquivo = arq.readLine();
            if (linhaDoArquivo != null) {
                String c = linhaDoArquivo.substring(0, 2);  // separa índices de 0 a 1
                String r = linhaDoArquivo.substring(2, 7);  // separa índices de 2 a 6
                String n = linhaDoArquivo.substring(7, 37); // separa índices de 7 a 36
                String sqn = linhaDoArquivo.substring(37, 39);  // separa 2 caracteres
                int qn = Integer.parseInt(sqn);                 // converte sqn para int
                setNotas(new double[15]);   // cria vetor de notas do estudante com 15 posições

                // armazenamos os valores já conhecidos para os atributos de Estudante:
                setCurso(c);
                setRa(r);
                setNome(n);
                setQuantasNotas(qn);

                // separamos da linha lida do arquivo, tantas notas quanto o valor de qn
                // e cada nota separada é atribuída a uma posição do vetor interno notas
                int inicioNota = 39; // posição inicial da 1a nota
                for (int indNota = 0; indNota < qn; indNota++) {
                    String umaNota = linhaDoArquivo.substring(inicioNota, inicioNota + 4);
                    notas[indNota] = Double.parseDouble(umaNota); // converte para real
                    inicioNota += 4; // posiciona no início da próxima nota
                }
                return true;  // conseguimos ler uma linha e separar os atributos
            }
            else
                return false;   // erro na linha lida, está vazia
        }
        return false;   // arquivo fechado
    }

    public String formatoDeArquivo()    // retorna string com os atributos
    {                                   // em formato de linha de dados do arquivo
        Locale.setDefault(Locale.US);
        String saida =
                String.format("%2s%5s%30s%02d", curso, ra, nome, quantasNotas);

        // concatena as notas do Estudante na string "saida"
        for (int indNota = 0; indNota < quantasNotas; indNota++)
            if (notas[indNota] < 10.0)
               saida += String.format("0%3.1f", notas[indNota]);
            else
                saida += String.format("%4.1f", notas[indNota]);

        return saida+"\n";      // concatena final de linha
    }

    public void incluirNota(double qualNota) throws Exception
    {
        if (quantasNotas >= notas.length)   // tamanho físico do vetor
            throw new Exception("Notas em excesso!");

        notas[quantasNotas++] = qualNota;
    }

    public double mediaDasNotas() throws Exception
    {
        if (quantasNotas <= 0)
            throw new Exception("Quantidade de notas inválida para a média!");

        double soma = 0;
        for (int ind = 0; ind < quantasNotas; ind++)
            soma += notas[ind];
        return soma / quantasNotas;
    }

}
