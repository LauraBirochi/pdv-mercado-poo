package br.com.pdvmercado.controller;

import br.com.pdvmercado.model.Venda;
import br.com.pdvmercado.model.ItemVenda;
import br.com.pdvmercado.model.Produto;
import br.com.pdvmercado.model.Pagamento;
import br.com.pdvmercado.model.Usuario;
import br.com.pdvmercado.model.FechamentoCaixa;
import java.util.ArrayList;

/**
 * VendaController
 * Responsável por:
 * - Gerenciar o fluxo completo de uma venda
 * - Adicionar/remover itens do carrinho
 * - Processar o pagamento
 * - Dar baixa automática no estoque
 * - Implementar a interface FechamentoCaixa
 *
 * Implementa FechamentoCaixa - aplicação prática da INTERFACE.
 */
public class VendaController implements FechamentoCaixa {

    private ProdutoController produtoController;
    private Venda vendaAtual;                    
    private ArrayList<Venda> historicoVendas;    
    private int proximoIdVenda;

    // Construtor
    public VendaController(ProdutoController produtoController) {
        this.produtoController = produtoController;
        this.historicoVendas = new ArrayList<>();
        this.proximoIdVenda = 1;
    }

    // Sobre a Venda Atual

    /**
     * Inicia uma nova venda para o operador logado.
     */
    public void iniciarVenda(Usuario operador) {
        this.vendaAtual = new Venda(proximoIdVenda++, operador);
    }

    /**
     * Adiciona um produto ao carrinho da venda atual.
     */
    public String adicionarItem(int idProduto, int quantidade) {
        if (vendaAtual == null) {
            return "Erro: Nenhuma venda em andamento. Inicie uma venda primeiro.";
        }

        Produto produto = produtoController.buscarPorId(idProduto);
        if (produto == null) {
            return "Produto não encontrado.";
        }

        if (!produto.temEstoque(quantidade)) {
            return "Estoque insuficiente! Disponível: " + produto.getEstoque();
        }

        // Verifica se o produto já está no carrinho
        for (ItemVenda item : vendaAtual.getItens()) {
            if (item.getProduto().getId() == idProduto) {
                int novaQtd = item.getQuantidade() + quantidade;
                if (!produto.temEstoque(novaQtd)) {
                    return "Estoque insuficiente para a quantidade total desejada.";
                }
                item.setQuantidade(novaQtd);
                return "Quantidade atualizada: " + produto.getNome() + " x" + novaQtd;
            }
        }

        // Adiciona novo item ao carrinho
        ItemVenda novoItem = new ItemVenda(produto, quantidade);
        vendaAtual.adicionarItem(novoItem);
        return "Adicionado: " + produto.getNome() + " x" + quantidade;
    }

    /**
     * Remove um item do carrinho pelo índice.
     */
    public void removerItem(int indice) {
        if (vendaAtual != null) {
            vendaAtual.removerItem(indice);
        }
    }

    /**
     * Define a forma de pagamento da venda atual.
     */
    public void selecionarPagamento(Pagamento pagamento) {
        if (vendaAtual != null) {
            vendaAtual.setPagamento(pagamento);
        }
    }

    /**
     * Finaliza a venda atual e da baixa automatica no estoque
     */
    
    public String finalizarVenda() {
        if (vendaAtual == null) {
            return "Nenhuma venda em andamento.";
        }

        try {
            // Finaliza a venda (processa pagamento)
            String resultadoPagamento = vendaAtual.finalizar();

            // Para cada item do carrinho, reduz o estoque do produto
            for (ItemVenda item : vendaAtual.getItens()) {
                item.getProduto().reduzirEstoque(item.getQuantidade());
            }

            // Guarda no histórico
            historicoVendas.add(vendaAtual);

            // Gera o cupom
            String cupom = vendaAtual.gerarCupom();

            // Limpa a venda atual
            vendaAtual = null;

            return cupom + "\n\n" + resultadoPagamento;

        } catch (IllegalStateException e) {
            return "Erro: " + e.getMessage();
        }
    }

    /**
     * Cancela a venda atual sem baixar estoque.
     */
    public void cancelarVenda() {
        if (vendaAtual != null) {
            vendaAtual.cancelar();
            vendaAtual = null;
        }
    }

    // IMPLEMENTAÇÃO DA INTERFACE FechamentoCaixa

    /**
     * Fecha o caixa e gera relatório do turno.
     */
    @Override
    public String fecharCaixa() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("===========================================\n");
        relatorio.append("          RELATÓRIO DE FECHAMENTO         \n");
        relatorio.append("===========================================\n");
        relatorio.append("Total de vendas: ").append(getTotalTransacoes()).append("\n");
        relatorio.append(String.format("Valor total: R$ %.2f\n", calcularTotalVendas()));
        relatorio.append("-------------------------------------------\n");
        relatorio.append("VENDAS DO TURNO:\n");
        for (Venda v : historicoVendas) {
            relatorio.append("  ").append(v.toString()).append("\n");
        }
        relatorio.append("===========================================\n");
        return relatorio.toString();
    }

    /**
     * Calcula o total de todas as vendas do turno.
     */
    @Override
    public double calcularTotalVendas() {
        double total = 0;
        for (Venda v : historicoVendas) {
            total += v.calcularTotal();
        }
        return total;
    }

    /**
     * Retorna a quantidade de vendas realizadas.
     */
    @Override
    public int getTotalTransacoes() {
        return historicoVendas.size();
    }

    // getters

    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public ArrayList<Venda> getHistoricoVendas() {
        return historicoVendas;
    }

    public double getTotalCarrinho() {
        if (vendaAtual == null) return 0;
        return vendaAtual.calcularTotal();
    }
}
