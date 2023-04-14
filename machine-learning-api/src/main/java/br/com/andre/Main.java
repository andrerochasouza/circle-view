package br.com.andre;

import br.com.andre.api.aplicacao.HttpClientApi;

public class Main {
    public static void main(String[] args)  {

        HttpClientApi.initServer(4567, true, false, true);

    }
}