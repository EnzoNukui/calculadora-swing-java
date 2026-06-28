package br.com.enzonukui.calculadora;

import javax.swing.*;
import java.awt.*;

public class CalculadoraTela {

    // Atributos
    private JFrame janela;
    private JTextField display;
    private JPanel painelBotoes;
    private Calculadora calculadora;
    private boolean resultadoExibido;

    // Construtor
    public CalculadoraTela() {
        calculadora = new Calculadora();
        resultadoExibido = false;

        criarJanela();
        criarDisplay();
        criarBotoes();

        janela.setVisible(true);
    }

    // Métodos
    private void criarJanela() {
        janela = new JFrame("Calculadora");

        janela.setSize(380, 520);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());
        janela.setResizable(false);
    }

    private void criarDisplay() {
        display = new JTextField();

        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.LEFT);

        janela.add(display, BorderLayout.NORTH);
    }

    private void criarBotoes() {
        painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(5, 4, 5, 5));

        String[] botoes = {
                "C", "<-", "(", ")",
                "7", "8", "9", "/",
                "4", "5", "6", "x",
                "1", "2", "3", "-",
                "0", ".", "+", "="
        };

        for (String textoBotao : botoes) {
            JButton botao = new JButton(textoBotao);

            if (textoBotao.equals("+") || textoBotao.equals("=")) {
                botao.setFont(new Font("Arial", Font.BOLD, 26));
            } else {
                botao.setFont(new Font("Arial", Font.BOLD, 22));
            }

            botao.addActionListener(e -> tratarClique(textoBotao));

            painelBotoes.add(botao);
        }

        janela.add(painelBotoes, BorderLayout.CENTER);
    }

    private void tratarClique(String textoBotao) {
        if (resultadoExibido && textoBotao.matches("[0-9]")) {
            calculadora.limpar();
            resultadoExibido = false;
        }

        if (textoBotao.matches("[0-9]")) {
            calculadora.adicionarNumero(textoBotao);
            atualizarDisplay();

        } else if (textoBotao.equals("+")
                || textoBotao.equals("-")
                || textoBotao.equals("x")
                || textoBotao.equals("/")) {

            calculadora.adicionarOperador(textoBotao);
            resultadoExibido = false;
            atualizarDisplay();

        } else if (textoBotao.equals(".")) {
            if (resultadoExibido) {
                calculadora.limpar();
                resultadoExibido = false;
            }

            calculadora.adicionarPonto();
            atualizarDisplay();

        } else if (textoBotao.equals("(") || textoBotao.equals(")")) {
            if (resultadoExibido) {
                calculadora.limpar();
                resultadoExibido = false;
            }

            calculadora.adicionarParenteses(textoBotao);
            atualizarDisplay();

        } else if (textoBotao.equals("C")) {
            calculadora.limpar();
            resultadoExibido = false;
            atualizarDisplay();

        } else if (textoBotao.equals("DEL")) {
            calculadora.apagarUltimo();
            resultadoExibido = false;
            atualizarDisplay();

        } else if (textoBotao.equals("=")) {
            calcularResultado();
        }
    }

    private void atualizarDisplay() {
        display.setText(calculadora.getExpressao());
    }

    private void calcularResultado() {
        try {
            double resultado = calculadora.calcular();

            calculadora.setExpressao(String.valueOf(resultado));
            display.setText(calculadora.getExpressao());

            resultadoExibido = true;

        } catch (Exception erro) {
            display.setText("Erro");
            calculadora.limpar();
            resultadoExibido = true;
        }
    }
}