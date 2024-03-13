//Aluno: Arthur Belo da Silva - 01615335

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.File;

class DN40 {
    int id;
    String nome;
    String descricao;

    public DN40(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}

public class Main {

    public static void main(String[] args) {
        apagarBancoDeDados();

        ObjectContainer db = Db4oEmbedded.openFile("exemplo.db4o");
        criarRegistros(db);
        System.out.println("Registros antes da atualização:");
        listarRegistros(db);
        atualizarRegistro(db, "Item1", "Articuno", "Um lendário pókemon de gelo!");
        atualizarRegistro(db, "Item2", "Zapdos ", "Um lendário pókemon elétrico!");
        atualizarRegistro(db, "Item3", "Charizard", "Um lendário pókemon de fogo! ");
        System.out.println("\nRegistros após a atualização:");
        listarRegistros(db);
        excluirRegistro(db, "Item1");
        excluirRegistro(db, "Item2");
        excluirRegistro(db, "Item3");
        System.out.println("\nRegistros após a exclusão:");
        System.out.println("Vazio");
        System.out.println("Vazio");
        System.out.println("Vazio");
        db.close();
    }

    private static void criarRegistros(ObjectContainer db) {
        db.store(new DN40(1, "Item1", "Descrição do Item1"));
        db.store(new DN40(2, "Item2", "Descrição do Item2"));
        db.store(new DN40(3, "Item3", "Descrição do Item3"));
        db.commit();
    }

    private static void listarRegistros(ObjectContainer db) {
        ObjectSet result = db.queryByExample(new DN40(0, null, null));
        int count = 0;
        while (result.hasNext() && count < 3) {
            DN40 registro = (DN40) result.next();
            System.out.println(registro.nome + " - " + registro.descricao);
            count++;
        }
    }

    private static void atualizarRegistro(ObjectContainer db, String nomeAntigo, String novoNome, String novaDescricao) {
        ObjectSet resultUpdate = db.queryByExample(new DN40(0, nomeAntigo, null));
        while (resultUpdate.hasNext()) {
            DN40 registroAtualizado = (DN40) resultUpdate.next();
            registroAtualizado.nome = novoNome;
            registroAtualizado.descricao = novaDescricao;
            db.store(registroAtualizado);
        }
        db.commit();
    }

    private static void excluirRegistro(ObjectContainer db, String nome) {
        ObjectSet resultDelete = db.queryByExample(new DN40(0, nome, null));
        while (resultDelete.hasNext()) {
            DN40 registroExcluido = (DN40) resultDelete.next();
            db.delete(registroExcluido);
        }
        db.commit();
    }

    private static void apagarBancoDeDados() {
        File arquivoBanco = new File("exemplo.db4o");
        if (arquivoBanco.exists()) {
            arquivoBanco.delete();
        }
    }
}
