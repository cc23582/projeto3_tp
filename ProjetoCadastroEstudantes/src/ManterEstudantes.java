import java.io.*;

public class ManterEstudantes implements ManterDados
{
    // Declaração do vetor de Estudantes e demais atributos
    // necessários para manutenção de vetores de Estudante

    private Estudante[] dados;  // vetor onde instâncias de Estudante serão armazenadas
    private int quantosDados;   // tamanho lógico do vetor dados
    private Situacao situacao;  // situação atual do programa
    private int posicaoAtual;   // qual índice estamos visitando
    private int ondeEsta;       // indica onde no vetor está o dado procurado ou onde deveria estar

    public ManterEstudantes(int tamanhoFisico)
    {
        dados = new Estudante[tamanhoFisico];
        quantosDados = 0;
        situacao = Situacao.navegando;
        posicaoAtual = -1;  // não visita nenhuma posição ainda
    }

    @Override
    public void lerDados(String nomeArquivo) throws FileNotFoundException, Exception
    {
        boolean parar = false;
        BufferedReader arquivoDeEntrada = null;
        try
        {
            arquivoDeEntrada = new BufferedReader(
                    new FileReader(nomeArquivo)
            );
        }
        catch (FileNotFoundException erroDeArquivo)
        {
            System.out.println("Não conseguiu abrir o arquivo");
            parar = true;
        }

        try
        {
            while (! parar)
            {
                Estudante novoEstudante = new Estudante();
                if (novoEstudante.leuLinhaDoArquivo(arquivoDeEntrada))
                {
                    incluirNoFinal(novoEstudante);
                }
                else
                    parar = true;
            }
            arquivoDeEntrada.close();
        }
        catch (Exception erroDeLeitura)
        {
            System.out.println("Erro na leitura dos dados do arquivo" + erroDeLeitura);
        }
    }

    @Override
    public void gravarDados(String nomeArquivo) throws IOException
    {
        BufferedWriter arquivoDeSaida = new BufferedWriter(
                new FileWriter(nomeArquivo)
        );

        // percorremos o vetor dados para gravar os estudantes nele armazenados
        // no arquivo de saída
        for (int indice =0; indice < quantosDados; indice++)
            arquivoDeSaida.write(dados[indice].formatoDeArquivo());

        arquivoDeSaida.close();
    }

    @Override
    public void incluirNoFinal(Estudante novoDado)
    {
        // if (quantosDados >= dados.length)
        //    expandirVetor();        // aumenta tamanho físico do vetor

        dados[quantosDados++] = novoDado;
    }

    @Override
    public void incluirEm(Estudante novoDado, int posicaoDeInclusao) throws Exception
    {
        // if (quantosDados >= dados.length)
        //    expandirVetor();        // aumenta tamanho físico do vetor

        if (posicaoDeInclusao < 0 || posicaoDeInclusao > quantosDados)
            throw new Exception("Posição inválida para inclusão!");

        for (int indice = quantosDados; indice > posicaoDeInclusao; indice--)
            dados[indice] = dados[indice-1];

        dados[posicaoDeInclusao] = novoDado;
        quantosDados++;
    }

    @Override
    public void excluir(int posicaoDeExclusao) throws IndexOutOfBoundsException
    {
        if (posicaoDeExclusao < 0 || posicaoDeExclusao >= quantosDados)
            throw new IndexOutOfBoundsException("Posição inválida para acesso aos dados!");

        quantosDados--;
        for (int indice = posicaoDeExclusao; indice < quantosDados; indice++)
            dados[indice] = dados[indice+1];
        dados[quantosDados] = null;
    }

    @Override
    public Estudante valorDe(int posicaoDeAcesso) throws IndexOutOfBoundsException
    {
        if (posicaoDeAcesso >= 0 && posicaoDeAcesso < quantosDados)
            return dados[posicaoDeAcesso];

        throw new IndexOutOfBoundsException("Índice inválido!");
    }

    @Override
    public void alterar(Estudante novosDados, int posicaoDeAlteracao) throws IndexOutOfBoundsException
    {
        if (posicaoDeAlteracao >= 0 && posicaoDeAlteracao < quantosDados)
            dados[posicaoDeAlteracao] = novosDados;
        else
            throw new IndexOutOfBoundsException("Índice inválido para alteração!");
    }

    @Override
    public boolean existe(Estudante procurado)
    {
        boolean achou = false;
        int inicio = 0;
        int fim = quantosDados - 1;
        while (! achou && inicio <= fim)
        {
            ondeEsta = (inicio + fim) / 2;
            if (dados[ondeEsta].getRa().compareTo(procurado.getRa()) == 0 )
                achou = true;
            else
                if (dados[ondeEsta].getRa().compareTo(procurado.getRa()) > 0)
                    fim = ondeEsta - 1;
                else
                    inicio = ondeEsta + 1;
        }
        if (!achou)
            ondeEsta = inicio;
        return achou;
    }

    public int getOnde() {
        return ondeEsta;
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    @Override
    public void ordenar() {
        for (int lento=0; lento < quantosDados; lento++)
            for (int rapido= lento+1; rapido < quantosDados; rapido++)
                if (dados[lento].getRa().compareTo(dados[rapido].getRa()) > 0)
                {
                    Estudante aux = dados[lento];
                    dados[lento] = dados[rapido];
                    dados[rapido] = aux;
                }
    }

    @Override
    public boolean estaVazio() {
        return quantosDados == 0;
    }

    @Override
    public Situacao getSituacao() {
        return situacao;
    }

    @Override
    public void setSituacao(Situacao novaSituacao) {
        situacao = novaSituacao;
    }

    @Override
    public int getTamanho() {
        return quantosDados;
    }

    @Override
    public boolean estaNoInicio() {
        return posicaoAtual == 0;
    }

    @Override
    public boolean estaNoFim() {
        return posicaoAtual == quantosDados-1;
    }

    @Override
    public void irAoInicio() {
        posicaoAtual = 0;
    }

    @Override
    public void irAoFim() {
        posicaoAtual = quantosDados - 1;
    }

    @Override
    public void irAoAnterior() {
        if (posicaoAtual > 0)
            posicaoAtual--;
    }

    @Override
    public void irAoProximo() {
        if (posicaoAtual < quantosDados - 1)
            posicaoAtual++;
    }
}
