package android.pratica5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public final class DatabaseSingleton {
    protected SQLiteDatabase db;
    private final String NOME_BANCO = "exemplo_bd";
    private static DatabaseSingleton INSTANCE;

    private final String[] SCRIPT_DATABASE_CREATE = new String[]{
            "CREATE TABLE Checkin (Local TEXT PRIMARY KEY, qtdVisitas INTEGER NOT NULL," +
            "cat INTEGER NOT NULL, latitude TEXT NOT NULL," +
            "longitude TEXT NOT NULL, CONSTRAINT fkey0 FOREIGN KEY (cat)" +
            "REFERENCES Categoria (idCategoria));",

            "CREATE TABLE Categoria (idCategoria INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL);",

            "INSERT INTO Checkin (Local, qtdVisitas, cat, latitude, longitude) VALUES ('Colmeia', 10, 1, '40.456123', '20.468579');",
            "INSERT INTO Checkin (Local, qtdVisitas, cat, latitude, longitude) VALUES ('Manikomio', 2, 4, '-40.456123', '20.446859');",

            "INSERT INTO Categoria (nome) VALUES ('Restaurante');",
            "INSERT INTO Categoria (nome) VALUES ('Bar');",
            "INSERT INTO Categoria (nome) VALUES ('Cinema');",
            "INSERT INTO Categoria (nome) VALUES ('Universidade');",
            "INSERT INTO Categoria (nome) VALUES ('Estádio');",
            "INSERT INTO Categoria (nome) VALUES ('Parque');",
            "INSERT INTO Categoria (nome) VALUES ('Outros');"
    };

    private DatabaseSingleton() {
        //Obtem contexto da aplicação usando a classe criada para essa finalidade
        Context ctx = MyApp.getAppContext();

        // Abre o banco de dados já existente ou então cria um banco novo
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        //busca por tabelas existentes no banco igual "show tables" do MySQL
        //SELECT * FROM sqlite_master WHERE type = "table"
        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");

        //Cria tabelas do banco de dados caso o mesmo estiver vazio.
        //bancos criados pelo método openOrCreateDatabase() POSSUEM UMA TABELA
        //PADRÃO "android_metadata"
        if (c.getCount() == 1) {
            for (int i = 0; i < SCRIPT_DATABASE_CREATE.length; i++) {
                db.execSQL(SCRIPT_DATABASE_CREATE[i]);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
        }
        c.close();
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    // Insere um novo registro
    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);
        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    // Atualiza registros
    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);
        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    // Deleta registros
    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);
        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    // Busca registros
    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;
        if (!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        Log.i("BANCO_DADOS", "Realizou um select e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    // Abre conexão com o banco
    private void abrir() {
        Context ctx = MyApp.getAppContext();
        if (!db.isOpen()) {
            // Abre o banco de dados já existente
            db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
            Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
        } else {
            Log.i("BANCO_DADOS", "Conexao com o banco já estava aberta.");
        }
    }

    //Retorna a única instância existente dessa classe para qualquer parte do projeto
    public static DatabaseSingleton getInstance() {
        if (INSTANCE == null)
            INSTANCE = new DatabaseSingleton();
        INSTANCE.abrir(); //abre conexão se estiver fechada
        return INSTANCE;
    }

    // Fecha conexão com o banco
    public void fechar() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }
}
