package br.com.pdvmercado;

import br.com.pdvmercado.controller.SistemaController;
import br.com.pdvmercado.view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main.java - Ponto de entrada do sistema PDV Mercado
 *
 * Esta classe inicializa o sistema e abre a tela de login.
 *
 * Padrão MVC:
 * - Cria o SistemaController (camada Controller)
 * - Abre a LoginView (camada View)
 * - O Model é gerenciado pelos Controllers
 *
 * SwingUtilities.invokeLater garante que a interface gráfica
 * seja iniciada na thread correta (Event Dispatch Thread).
 */
public class Main {

    public static void main(String[] args) {

        // Aplica o look and feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se não conseguir, usa o padrão do Swing (sem problema)
            System.out.println("Look and Feel padrão em uso.");
        }

        // Inicializa a aplicação na thread da interface gráfica (EDT)
        SwingUtilities.invokeLater(() -> {

            // 1. Cria o controller central do sistema
            SistemaController sistemaController = new SistemaController();

            // 2. Cria e exibe a tela de login
            LoginView loginView = new LoginView(sistemaController);
            loginView.setVisible(true);

            System.out.println("=== PDV Mercado iniciado com sucesso! ===");
            System.out.println("Credenciais de acesso:");
            System.out.println("  Gerente -> login: admin   | senha: admin123");
            System.out.println("  Caixa   -> login: joao    | senha: joao123");
        });
    }
}
