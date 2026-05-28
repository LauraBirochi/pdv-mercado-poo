package br.com.pdvmercado.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Classe Venda
 *
 * Representa uma venda completa no PDV.
 * Contém o carrinho de compras (lista de itens), o cliente,
 * o operador e a forma de pagamento.
 *
 * Regra de negócio: A venda só pode ser finalizada após
 * a seleção de uma forma de pagamento.
 */
public class Venda {

    private int id;
    private ArrayList<ItemVenda> itens;     // Carrinho de compras
    private Cliente cliente;                 // Cliente (pode ser nulo)
    private Usuario operador;               // Quem está operando o caixa
    private Pagamento pagamento;            // Forma de pagamento selecionada
    private LocalDateTime dataHora;         // Data e hora da venda
    private boolean finalizada;             // Status da venda

    // Construtor
    public Venda(int id, Usuario operador) {
        this.id = id;
        this.operador = operador;
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.finalizada = false;
    }

    // =============================================
    // MÉTODOS DE NEGÓCIO (carrinho e venda)
    // =============================================

    /**
     * Adiciona um item ao carrinho de compras.
     * @param item item a ser adicionado
     */
    public void adicionarItem(ItemVenda item) {
        if (finalizada) {
            throw new IllegalStateException("Não é possível alterar uma venda já finalizada.");
        }
        itens.add(item);
    }

    /**
     * Remove um item do carrinho pelo índice.
     * @param indice posição do item na lista
     */
    public void removerItem(int indice) {
        if (finalizada) {
            throw new IllegalStateException("Não é possível alterar uma venda já finalizada.");
        }
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
        }
    }

    /**
     * Calcula o valor total de todos os itens do carrinho.
     * @return total da venda
     */
    public double calcularTotal() {
        double total = 0;
        for (ItemVenda item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    /**
     * Finaliza a venda.
     * Regra de negócio: só pode finalizar com pagamento selecionado.
     * @return resultado do processamento do pagamento
     */
    public String finalizar() {
        if (pagamento == null) {
            throw new IllegalStateException("Selecione uma forma de pagamento antes de finalizar.");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }
        this.finalizada = true;
        return pagamento.processar();
    }

    /**
     * Cancela a venda atual e limpa o carrinho.
     */
    public void cancelar() {
        this.itens.clear();
        this.pagamento = null;
        this.finalizada = false;
    }

    /**
     * Gera o cupom (recibo) da venda.
     * @return texto formatado do cupom
     */
    public String gerarCupom() {
        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("          CUPOM FISCAL - PDV MERCADO      \n");
        sb.append("===========================================\n");
        sb.append("Venda Nº: ").append(id).append("\n");
        sb.append("Data: ").append(dataHora.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("Operador: ").append(operador.getNome()).append("\n");
        if (cliente != null) {
            sb.append("Cliente: ").append(cliente.getNome()).append("\n");
        }
        sb.append("-------------------------------------------\n");
        sb.append("ITENS:\n");
        for (ItemVenda item : itens) {
            sb.append("  ").append(item.toString()).append("\n");
        }
        sb.append("-------------------------------------------\n");
        sb.append(String.format("TOTAL: R$ %.2f\n", calcularTotal()));
        sb.append("-------------------------------------------\n");
        if (pagamento != null) {
            sb.append("Pagamento: ").append(pagamento.toString()).append("\n");
        }
        sb.append("===========================================\n");
        sb.append("         Obrigado pela preferência!       \n");
        sb.append("===========================================\n");
        return sb.toString();
    }

    // =============================================
    // GETTERS E SETTERS
    // =============================================

    public int getId() {
        return id;
    }

    public ArrayList<ItemVenda> getItens() {
        return itens;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getOperador() {
        return operador;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        if (finalizada) {
            throw new IllegalStateException("Venda já finalizada.");
        }
        this.pagamento = pagamento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    @Override
    public String toString() {
        return String.format("Venda #%d | Total: R$ %.2f | Finalizada: %s",
                id, calcularTotal(), finalizada ? "Sim" : "Não");
    }
}
