import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class AVL_tempo
{
static class Detalhes_cartao implements Comparable<Detalhes_cartao>
{
    String numero_cartao,data_cartao;

    public Detalhes_cartao(String numero , String data)
    {
        this.numero_cartao = numero;
        this.data_cartao = data;
    }

    @Override public int compareTo(Detalhes_cartao cartao)
    {
        String compara_numero = ((Detalhes_cartao)cartao).numero_cartao;

        return 
        this.numero_cartao.compareTo(compara_numero);
    }


}

  static class Usuario{

    String nome_user;
    ArrayList<Detalhes_cartao> cartoes = new ArrayList<>();


    public Usuario(String nome , Detalhes_cartao cartao)
    {
        this.nome_user = nome;
        this.cartoes.add(cartao);
    }

    public void  mostra_cartao(Usuario user1)
    {
        for(int i = 0; i < user1.cartoes.size() ; i++)
        {
            System.out.print(user1.cartoes.get(i).numero_cartao + " ");
            System.out.println(user1.cartoes.get(i).data_cartao);
        }
        System.out.println("FIM");
    }

}


static class No {
    Usuario user;
    int altura;
    No esquerda, direita;
 
    No(Usuario user1) 
    {
        user = user1;
        altura = 1;
    }

    void atualiza_cartao(No no , String cartao , String data)
    {
        int encontrado = 0;

       for(int i = 0; i < no.user.cartoes.size() ; i++)
       {
            if(no.user.cartoes.get(i).numero_cartao.equals(cartao))
            {
               //System.out.println("CARTAO ATUALIZADO");
                encontrado++;
                no.user.cartoes.get(i).data_cartao = data;
            }
        }

        if(encontrado == 0)
        {
            //System.out.println("NOVO CARTAO INSERIDO");
            Detalhes_cartao detalhes = new Detalhes_cartao(cartao , data);
            no.user.cartoes.add(detalhes);
            Collections.sort(user.cartoes);
        }
    }

    No printInOrder(ArrayList<No> elementos , No raiz)
    {
    if(raiz.esquerda != null )
    raiz.esquerda = raiz.esquerda.printInOrder(elementos , raiz.esquerda); // Left

    elementos.add(raiz); // Node

    if( raiz.direita != null )
    raiz.direita = raiz.direita.printInOrder(elementos , raiz.direita); // Right

    return raiz;
    }
 
} 
 
static class Arvore 
{
    No raiz;
 
    //Muda altura do no
    int altura(No no) {
        if (no == null)
            return 0;
 
        return no.altura;
    }
 
    // da fator de equilibrio da arvore.
    int equilibrio(No N) {
        if (N == null)
            return 0;
    
        return altura(N.esquerda) - altura(N.direita);
    }

    No rotacao_direita(No y) 
    {
        No x = y.esquerda;
        No filho = x.direita;
 
        //x fica no sitio de y e y fica com o filho de x.
        x.direita = y;
        y.esquerda = filho;
 
        //Por as alturas novas
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda) , altura(x.direita)) + 1;
 
        
        return x;
    }

 

    No rotacao_esquerda(No x) 
    {
        No y = x.direita;
        No filho = y.esquerda;
 
        //y fica no sitio de x e x fica com o filho de y. 
        y.esquerda = x;
        x.direita = filho;
 
        x.altura = Math.max(altura(x.esquerda) , altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    No consulta(No no, String user) 
    {
        //Chegamos ao fim e nao foi encontrado
        if (no == null)
        {
            //System.out.println("NAO ENCONTRADO");
            return no;
        }
        //Novo no e mais pequeno que o atual ir para a esquerda
        if (no.user.nome_user.compareTo(user) > 0)
            no.esquerda = consulta(no.esquerda, user);
        

        //Novo no e maior que o atual, ir para a direita
        else if (no.user.nome_user.compareTo(user) < 0)
            no.direita = consulta(no.direita, user);


        //Encontramos um no igual    
        else{
            //no.user.mostra_cartao(no.user);
            return no;
        }
        return no;
    }


    No acrescenta(No no, Usuario user) 
    {
        //Encontramos espaco entao criamos novo No.
        if (no == null)
        {
           // System.out.println("NOVO UTILIZADOR CRIADO");
            return (new No(user));
        } 

        //Novo no e mais pequeno que o atual ir para a esquerda
        if (no.user.nome_user.compareTo(user.nome_user) > 0)
            no.esquerda = acrescenta(no.esquerda, user);

        //Novo no e maior que o atual, ir para a direita
        else if (no.user.nome_user.compareTo(user.nome_user) < 0)
            no.direita = acrescenta(no.direita, user);

        //Encontramos um no igual    
        else
        {
            no.atualiza_cartao(no , user.cartoes.get(0).numero_cartao , user.cartoes.get(0).data_cartao);
            return no;
        }
 
        //Atualizar a altura.
        no.altura = 1 + Math.max(altura(no.esquerda),
                              altura(no.direita));
        //Receber fator de equilibrio
        int balance = equilibrio(no);
        

        // Caso direita esquerda
        if (balance < -1 && no.direita.user.nome_user.compareTo(user.nome_user) > 0 ) 
        {
            no.direita = rotacao_direita(no.direita);
            return rotacao_esquerda(no);
        }

        // Caso direita direita
        if (balance < -1 && no.direita.user.nome_user.compareTo(user.nome_user) < 0)
        return rotacao_esquerda(no);
         
        //Caso Esquerda Esquerda
        if (balance > 1 && no.esquerda.user.nome_user.compareTo(user.nome_user) > 0)
            return rotacao_direita(no);
 
 
        // Caso esquerda direita
        if (balance > 1 && no.esquerda.user.nome_user.compareTo(user.nome_user) < 0) 
        {
            no.esquerda = rotacao_esquerda(no.esquerda);
            return rotacao_direita(no);
        }
 
        return no;
    }
}
 
    static String le_parametros(Scanner sc)
    {
        String str;  
         
        try{
                
        str = sc.nextLine();

        }
        //Se o valor for um valor causar um erro, ira ser avisado ao usuario que o valor nao e valido.
        catch (java.util.InputMismatchException e){
            System.out.printf("Valor Introduzido nao e valido.");
            return null;
        }
        
        return str;
    }


    static void opcoes(Arvore raiz , int elementos)
    {   
        long tempo = 0;


        for(int x = 0 ; x < (elementos * 0.1) ; x++)
        {
            Random r = new Random();
            int randomNumber = r.nextInt(elementos/10);
            Random r1 = new Random();
            int randomNumber1 = r1.nextInt(elementos/10);
            Random r2 = new Random();
            int randomNumber2 = r2.nextInt(elementos/10);

            Detalhes_cartao detalhes = new Detalhes_cartao(String.valueOf(randomNumber) , String.valueOf(randomNumber1));
            Usuario user1 = new Usuario(String.valueOf(randomNumber2) , detalhes);
            long start = System.nanoTime();
            raiz.raiz = raiz.acrescenta(raiz.raiz , user1);
            long end = System.nanoTime();
            tempo += end-start;

        }
        for(int y = 0 ; y < (elementos * 0.9) ; y++)
        {
            Random r = new Random();
            int randomNumber = r.nextInt(elementos/10);
            long start = System.nanoTime();
            raiz.consulta(raiz.raiz , String.valueOf(randomNumber));
            long end = System.nanoTime();
            tempo += end-start;

    }

    System.out.println(tempo);
}


    public static void main(String[] args) 
    {   

        Arvore tree = new Arvore();

        opcoes( tree , 100000);
    }
}
