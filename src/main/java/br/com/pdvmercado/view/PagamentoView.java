package br.com.pdvmercado.view;

import br.com.pdvmercado.controller.SistemaController;
import br.com.pdvmercado.model.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Tela de seleção e processamento de pagamento.
 */
public class PagamentoView extends JDialog {

    private SistemaController sistemaController;
    private CaixaView caixaView;

    // Componentes
    private JLabel labelTotal;
    private JPanel painelOpcoes;
    private ButtonGroup grupoPagamento;
    private JRadioButton radioDinheiro;
    private JRadioButton radioCartaoDebito;
    private JRadioButton radioCartaoCredito;
    private JRadioButton radioPix;

    // Campos específicos por forma de pagamento
    private JTextField campoValorRecebido;   // Dinheiro
    private JTextField campoDigitosCartao;   // Cartão
    private JTextField campoParcelas;        // Crédito
    private JTextField campoChavePix;        // Pix
    private JPanel painelDinheiro, painelCartao, painelPix;

    private double totalVenda;

    // Construtor
    public PagamentoView(SistemaController sistemaController, CaixaView caixaView) {
        super(caixaView, "Pagamento", true); // Dialog modal
        this.sistemaController = sistemaController;
        this.caixaView = caixaView;
        this.totalVenda = sistemaController.getVendaController().getTotalCarrinho();
        configurarJanela();
        criarComponentes();
    }

    // Configuracao da Janela

    private void configurarJanela() {
        setSize(480, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 45));
        setLayout(new BorderLayout(10, 10));
    }

    // Componentes

    private void criarComponentes() {
        // --- Cabeçalho ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.setBackground(new Color(35, 35, 52));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        labelTotal = new JLabel(String.format("Total a Pagar: R$ %.2f", totalVenda));
        labelTotal.setFont(new Font("Arial", Font.BOLD, 20));
        labelTotal.setForeground(new Color(100, 220, 120));
        painelTopo.add(labelTotal);

        add(painelTopo, BorderLayout.NORTH);

        // --- Painel central com opções de pagamento ---
        painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new BoxLayout(painelOpcoes, BoxLayout.Y_AXIS));
        painelOpcoes.setBackground(new Color(30, 30, 45));
        painelOpcoes.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // RadioButtons de forma de pagamento
        grupoPagamento = new ButtonGroup();

        radioDinheiro = criarRadio("Dinheiro");
        radioCartaoDebito = criarRadio("Cartão de Débito");
        radioCartaoCredito = criarRadio("Cartão de Crédito");
        radioPix = criarRadio("Pix");

        grupoPagamento.add(radioDinheiro);
        grupoPagamento.add(radioCartaoDebito);
        grupoPagamento.add(radioCartaoCredito);
        grupoPagamento.add(radioPix);

        painelOpcoes.add(new JLabel(" "));
        painelOpcoes.add(criarLabel("Selecione a forma de pagamento:"));
        painelOpcoes.add(Box.createVerticalStrut(8));
        painelOpcoes.add(radioDinheiro);
        painelOpcoes.add(radioCartaoDebito);
        painelOpcoes.add(radioCartaoCredito);
        painelOpcoes.add(radioPix);
        painelOpcoes.add(Box.createVerticalStrut(15));

        // Painéis de detalhes de cada forma (aparecem conforme seleção)
        painelDinheiro = criarPainelDinheiro();
        painelCartao = criarPainelCartao();
        painelPix = criarPainelPix();

        painelOpcoes.add(painelDinheiro);
        painelOpcoes.add(painelCartao);
        painelOpcoes.add(painelPix);

        // Listeners para mostrar/ocultar painéis
        radioDinheiro.addActionListener(e -> alternarPaineis("DINHEIRO"));
        radioCartaoDebito.addActionListener(e -> alternarPaineis("CARTAO"));
        radioCartaoCredito.addActionListener(e -> {
            alternarPaineis("CARTAO");
            campoParcelas.setEnabled(true);
        });
        radioPix.addActionListener(e -> alternarPaineis("PIX"));

        // Seleciona dinheiro por padrão
        radioDinheiro.setSelected(true);
        alternarPaineis("DINHEIRO");

        add(new JScrollPane(painelOpcoes), BorderLayout.CENTER);

        // --- Painel inferior com botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        painelBotoes.setBackground(new Color(35, 35, 52));

        JButton btnConfirmar = new JButton("Confirmar Pagamento");
        estilizarBotao(btnConfirmar, new Color(60, 160, 90));
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar.addActionListener(this::confirmarPagamento);
        painelBotoes.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBotao(btnCancelar, new Color(180, 60, 60));
        btnCancelar.addActionListener(e -> dispose());
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelDinheiro() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        p.add(criarLabel("Valor recebido: R$"));
        campoValorRecebido = new JTextField(String.format("%.2f", totalVenda), 10);
        estilizarCampo(campoValorRecebido);
        p.add(campoValorRecebido);
        return p;
    }

    private JPanel criarPainelCartao() {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setOpaque(false);
        p.add(criarLabel("Últimos 4 dígitos:"));
        campoDigitosCartao = new JTextField("0000", 6);
        estilizarCampo(campoDigitosCartao);
        p.add(campoDigitosCartao);

        p.add(criarLabel("Parcelas (crédito):"));
        campoParcelas = new JTextField("1", 4);
        campoParcelas.setEnabled(false);
        estilizarCampo(campoParcelas);
        p.add(campoParcelas);
        return p;
    }

    private JPanel criarPainelPix() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        p.add(criarLabel("Chave Pix:"));
        campoChavePix = new JTextField("mercado@pix.com", 18);
        estilizarCampo(campoChavePix);
        p.add(campoChavePix);
        return p;
    }

    private void alternarPaineis(String tipo) {
        painelDinheiro.setVisible("DINHEIRO".equals(tipo));
        painelCartao.setVisible("CARTAO".equals(tipo));
        painelPix.setVisible("PIX".equals(tipo));
        if ("CARTAO".equals(tipo) && radioCartaoDebito.isSelected()) {
            campoParcelas.setEnabled(false);
            campoParcelas.setText("1");
        }
        revalidate();
        repaint();
    }


    /**
     * Cria o objeto de pagamento correto conforme a forma selecionada.
     */
    private void confirmarPagamento(ActionEvent e) {
        try {
            Pagamento pagamento = null;

            if (radioDinheiro.isSelected()) {
                // Cria pagamento em dinheiro
                double valorRecebido = Double.parseDouble(
                        campoValorRecebido.getText().replace(",", "."));
                pagamento = new PagamentoDinheiro(totalVenda, valorRecebido);

            } else if (radioCartaoDebito.isSelected()) {
                // Cria pagamento com cartão de débito
                pagamento = new PagamentoCartao(totalVenda, "DEBITO", 1,
                        campoDigitosCartao.getText().trim());

            } else if (radioCartaoCredito.isSelected()) {
                // Cria pagamento com cartão de crédito
                int parcelas = Integer.parseInt(campoParcelas.getText().trim());
                pagamento = new PagamentoCartao(totalVenda, "CREDITO", parcelas,
                        campoDigitosCartao.getText().trim());

            } else if (radioPix.isSelected()) {
                // Cria pagamento via Pix
                pagamento = new PagamentoPix(totalVenda, campoChavePix.getText().trim());
            }

            if (pagamento == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento.");
                return;
            }

            // Define o pagamento e finaliza a venda
            sistemaController.getVendaController().selecionarPagamento(pagamento);
            String cupom = sistemaController.getVendaController().finalizarVenda();

            // Exibe o cupom/resultado
            JOptionPane.showMessageDialog(this, cupom, "Venda Finalizada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Fecha o dialog e avisa o CaixaView
            dispose();
            caixaView.vendaFinalizada();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valor inválido! Verifique os campos de pagamento.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Estilos

    private JRadioButton criarRadio(String texto) {
        JRadioButton radio = new JRadioButton(texto);
        radio.setForeground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 13));
        radio.setOpaque(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(180, 180, 200));
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(55, 55, 75));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 100)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    private void estilizarBotao(JButton botao, Color cor) {
        botao.setUI(new BasicButtonUI());
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
