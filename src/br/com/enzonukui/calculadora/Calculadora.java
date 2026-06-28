package br.com.enzonukui.calculadora;

import java.util.ArrayList;

public class Calculadora {

    //atributos
    private String expressao;
    private double resultado;

    //construtores
    public Calculadora() {
        this.expressao = "";
        this.resultado = 0;
    }

    public Calculadora(String expressao, double resultado) {
        this.expressao = expressao;
        this.resultado = 0;
    }

    //getters e setters


    public String getExpressao() {
        return expressao;
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public double getResultado() {
        return resultado;
    }

    private void setResultado(double resultado) {
        this.resultado = resultado;
    }

    //metodos da classe
    public void adicionarNumero(String numero) {
        expressao += numero;
    }

    public void adicionarOperador(String operador) {
        expressao += operador;
    }

    public void adicionarPonto() {
        expressao += ".";
    }

    public void apagarUltimo() {
        if (this.expressao != null && !this.expressao.isEmpty()) {
            this.expressao = this.expressao.substring(0, this.expressao.length() - 1);
        }
    }

    public void limpar() {
        expressao = "";
        resultado = 0;
    }

    public void adicionarParenteses(String parenteses) {
        this.expressao += parenteses;
    }

    public double calcular() throws Exception {
        validarExpressao();

        String expressaoTratada = removerEspacos(this.expressao);

        while (expressaoTratada.contains("(")) {
            expressaoTratada = resolverParenteses(expressaoTratada);
        }

        double resultadoFinal = resolverExpressaoSimples(expressaoTratada);

        setResultado(resultadoFinal);

        return this.resultado;
    }

    private void validarExpressao(){
        try {
            if (expressao == null || expressao.trim().isEmpty()) {
                throw new Exception("Digite uma expressão matemática.");
            }
            String expressaoTratada = removerEspacos(this.expressao);
            char ultimoCaractere = expressaoTratada.charAt(expressaoTratada.length() - 1);

            if (ehOperador(ultimoCaractere) || ultimoCaractere == '.') {
                throw new Exception("Expressão incompleta.");
            }

        } catch (Exception e) {
            System.out.println("Não foi possível indentificar uma expressão");
        }
    }
    private String removerEspacos(String expressao) {
        return expressao.replace(" ", "").replace(",", ".");
    }

    private String resolverParenteses(String expressao) throws Exception {
        int fechamento = expressao.indexOf(")");
        int abertura = expressao.lastIndexOf("(", fechamento);

        if (fechamento == -1 || abertura == -1) {
            throw new Exception("Parênteses inválidos.");
        }

        String conteudoParenteses = expressao.substring(abertura + 1, fechamento);

        if (conteudoParenteses.isEmpty()) {
            throw new Exception("Parênteses vazios.");
        }

        double resultadoParenteses = resolverExpressaoSimples(conteudoParenteses);

        return expressao.substring(0, abertura)
                + resultadoParenteses
                + expressao.substring(fechamento + 1);
    }

    private double resolverExpressaoSimples(String expressao){
       ArrayList<Double> numeros = new ArrayList<>();
       ArrayList<Character> operadores = new ArrayList<>();

        separarNumerosEOperadores(expressao, numeros, operadores);

        resolverMultiplicacaoDivisao(numeros, operadores);
        resolverSomaSubtracao(numeros, operadores);

        return numeros.get(0);
    }

    private void separarNumerosEOperadores(
            String expressao,
            ArrayList<Double> numeros,
            ArrayList<Character> operadores
    ){
        StringBuilder numeroAtual = new StringBuilder();

        try {
            for (int i = 0; i < expressao.length(); i++) {
                char caractere = expressao.charAt(i);

                if (Character.isDigit(caractere) || caractere == '.') {
                    numeroAtual.append(caractere);

                } else if (ehSinalNegativo(expressao, i)) {
                    numeroAtual.append(caractere);

                } else if (ehOperador(caractere)) {
                    if (numeroAtual.length() == 0) {
                        throw new Exception("Expressão inválida.");
                    }

                    numeros.add(Double.parseDouble(numeroAtual.toString()));
                    operadores.add(caractere);
                    numeroAtual.setLength(0);

                } else {
                    throw new Exception("Caractere inválido.");
                }
            }

            if (numeroAtual.length() == 0) {
                throw new Exception("Expressão incompleta.");
            }

            numeros.add(Double.parseDouble(numeroAtual.toString()));

        } catch (Exception e) {
            System.out.println("Nao foi possível separar os operadores, tente novamente");
        }
    }

    private boolean ehOperador(char caractere) {
        return caractere == '+'
                || caractere == '-'
                || caractere == 'x'
                || caractere == '/';
    }

    private double somar(double numero1, double numero2) {
        return numero1 + numero2;
    }

    private double subtrair(double numero1, double numero2) {
        return numero1 - numero2;
    }

    private double multiplicar(double numero1, double numero2) {
        return numero1 * numero2;
    }

    private double dividir(double numero1, double numero2) throws Exception {
        if (numero2 == 0) {
            throw new Exception("Não é possível dividir por zero.");
        }

        return numero1 / numero2;
    }

    private boolean ehSinalNegativo(String expressao, int posicao) {
        char caractereAtual = expressao.charAt(posicao);

        if (caractereAtual != '-') {
            return false;
        }

        if (posicao == 0) {
            return true;
        }

        char caractereAnterior = expressao.charAt(posicao - 1);

        return ehOperador(caractereAnterior) || caractereAnterior == '(';
    }

    private void resolverMultiplicacaoDivisao(
            ArrayList<Double> numeros,
            ArrayList<Character> operadores
    ){
        try {
            for (int i = 0; i < operadores.size(); i++) {
                char operador = operadores.get(i);

                if (operador == 'x' || operador == '/') {
                    double numero1 = numeros.get(i);
                    double numero2 = numeros.get(i + 1);
                    double resultadoOperacao;

                    if (operador == 'x') {
                        resultadoOperacao = multiplicar(numero1, numero2);
                    } else {
                        resultadoOperacao = dividir(numero1, numero2);
                    }

                    numeros.set(i, resultadoOperacao);
                    numeros.remove(i + 1);
                    operadores.remove(i);

                    i--;
                }
            }
        } catch (Exception e) {
            System.out.println("não foi possível resolver a multiplicação/divisão");
        }
    }

    private void resolverSomaSubtracao(
            ArrayList<Double> numeros,
            ArrayList<Character> operadores
    ){
        try {
            for (int i = 0; i < operadores.size(); i++) {
                char operador = operadores.get(i);

                double numero1 = numeros.get(i);
                double numero2 = numeros.get(i + 1);
                double resultadoOperacao;

                if (operador == '+') {
                    resultadoOperacao = numero1 + numero2;
                } else {
                    resultadoOperacao = numero1 - numero2;
                }

                numeros.set(i, resultadoOperacao);
                numeros.remove(i + 1);
                operadores.remove(i);

                i--;
            }
        } catch (Exception e) {
            System.out.println("Não foi possivel resolver a soma/subtração");
        }
    }



}
