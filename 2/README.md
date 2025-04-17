# Prática 2 (11/04)

Calculadora de IMC

c) Modifique a Tela 2 adicionando um botão que possibilite voltar a Tela 1 após a exibição dos relatório. 

i. Ao tentar voltar para a tela 1, usando intent e startActivity, a tela 2 executa o método onPause(). A tela 1 executa onCreate(). Então, a tela 2 faz onStop().
Ou seja, foi criado uma nova instÂncia da tela 1 e assim os dados digitados anteriormente não são visualizados.

Usando finish() para voltar a tela 1, é feito apenas onRestart() e a tela 2 termina com onDestroy(). Ou seja, a tela 2 é finalizada e a instância anterior da tela 1 volta ao topo da pilha.
Quando retorna a tela 1, os dados digitados anteriormente são visualizados, pois a mesma apenas ficou em segundo plano enquanto o usuário interagia com a tela 2.

Adicionando a flag REORDER_TO_FRONT, acontece semelhante ao finish(), mas a tela 2 não é destruída quando a tela 1 volta ao topo da pilha. Agora, a tela 2 está em segundo plano e pode ocorrer de executar outra instância mesmo que a primeira não tenha sido destruída.
Mas utilizando a flag novamente, isso é contornado.
