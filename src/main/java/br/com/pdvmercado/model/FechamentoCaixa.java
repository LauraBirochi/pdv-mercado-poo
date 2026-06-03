package br.com.pdvmercado.model;

/**
 * Contrato de FechamentoCaixa
 */
public interface FechamentoCaixa {

    /**
     * Realiza o fechamento do caixa.
     */
    String fecharCaixa();

    /**
     * Calcula o total de todas as vendas do turno.
     */
    double calcularTotalVendas();

    /**
     * Retorna a quantidade de vendas realizadas no turno.
     */
    int getTotalTransacoes();
}
