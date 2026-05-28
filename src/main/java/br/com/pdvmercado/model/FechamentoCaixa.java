package br.com.pdvmercado.model;

/**
 * Interface FechamentoCaixa
 *
 * Conceito de POO aplicado: INTERFACE
 *
 * Define o contrato obrigatório para qualquer classe
 * que realize o fechamento de caixa no sistema.
 *
 * Uso de interface aqui demonstra que diferentes classes podem
 * implementar o fechamento de caixa de formas distintas,
 * mas sempre respeitando o mesmo contrato.
 *
 * Requisito obrigatório do projeto acadêmico.
 */
public interface FechamentoCaixa {

    /**
     * Realiza o fechamento do caixa.
     * Deve consolidar as vendas do turno e gerar relatório.
     *
     * @return relatório do fechamento
     */
    String fecharCaixa();

    /**
     * Calcula o total de todas as vendas do turno.
     *
     * @return soma total das vendas
     */
    double calcularTotalVendas();

    /**
     * Retorna a quantidade de vendas realizadas no turno.
     *
     * @return número de vendas
     */
    int getTotalTransacoes();
}
