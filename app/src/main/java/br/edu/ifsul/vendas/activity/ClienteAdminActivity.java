package br.edu.ifsul.vendas.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.barcode.BarcodeCaptureActivity;
import br.edu.ifsul.vendas.model.Cliente;

public class ClienteAdminActivity extends AppCompatActivity {

    private static final String TAG = "produtoAdminActivity";
    private static final int RC_BARCODE_CAPTURE = 1, RC_GALERIA_IMAGE_PICK = 2;
    private EditText etCodigoDeBarras, etNome, etSobrenome, etCpf;
    private Button btSalvar;
    private ImageView imvFoto;
    private Cliente cliente;
    private byte[] fotoCliente = null; //foto do produto
    private Uri arquivoUri;
    private FirebaseDatabase database;
    private boolean flagInsertOrUpdate = true;
    private ProgressDialog mProgressDialog; //um modal de progressão (com uma animação da progressão)
    private ImageButton imbPesquisar;
    private ProgressBar pbFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_admin);


        //botão de voltar na barra superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //obtém a instância do database
        database = FirebaseDatabase.getInstance();

        //mapeia os componentes da UI
        etCodigoDeBarras = findViewById(R.id.etCodigoCliente);
        etNome = findViewById(R.id.etNomeClienteAdmin);
        etSobrenome = findViewById(R.id.etSobrenomeClienteAdmin);
        etCpf = findViewById(R.id.etCpfClienteAdmin);
        btSalvar = findViewById(R.id.btInserirClienteAdmin);
        imvFoto = findViewById(R.id.imvFoto);
        imbPesquisar = findViewById(R.id.imb_pesquisar);
        pbFoto = findViewById(R.id.pb_foto_cliente_admin);

        //busca a foto do produto na galeria
        imvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cria uma Intent
                //primeiro argumento: ação ACTION_PICK "pegar algum dado"
                //segundo argumento: refina a ação para arquivos de fotoProduto, na galeria do dispositivo, retornando um URI
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //inicializa uma Activity esperando o seu resultado. Neste caso, uma que forneca acesso a galeria de imagens do dispositivo.
                startActivityForResult(Intent.createChooser(intent,getString(R.string.titulo_janela_galeria_imagens)), RC_GALERIA_IMAGE_PICK);
            }
        });

        //pesquisa se o produto já está cadastrado no database
        imbPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCodigoDeBarras.getText().toString().isEmpty()){
                    buscarNoBanco(Long.valueOf(etCodigoDeBarras.getText().toString()));
                }
            }
        });

        //inicializa o objeto de modelo
        cliente = new Cliente();

        //salva o produto no database
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCodigoDeBarras.getText().toString().isEmpty() &&
                        !etNome.getText().toString().isEmpty() &&
                        !etSobrenome.getText().toString().isEmpty() &&
                        !etCpf.getText().toString().isEmpty() ){
                    if(fotoCliente != null){
                        uploadFotoDoCliente();
                    }else{
                        salvarCliente();
                    }
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_produtoadmin), R.string.snack_preencher_todos_campos, Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }

    private void buscarNoBanco(Long valueOf) {
    }


    private void salvarCliente() {
    }

    private void uploadFotoDoCliente() {
    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_produto_admin, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_barcode_admin:
                // launch barcode activity.
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //true liga a funcionalidade autofoco
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false); //true liga a lanterna (fash)
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;

            case R.id.menuitem_limparform_admin:
                limparForm();
                break;

            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    private void limparForm() {
    }



    /**
     * Converte um Bitmap em um array de bytes (bytes[])
     * @param bitmap
     * @return byte[]
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //criam um stream para ByteArray
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); //comprime a fotoProduto
        return outputStream.toByteArray(); //retorna a fotoProduto como um Array de Bytes (byte[])
    }

    /*Emite uma ProgressDialog
      Uma caixa com uma mensagem de progressão e uma barra de progressão
    */
    public void  showWait(){
        //cria e configura a caixa de diálogo e progressão
        mProgressDialog = new ProgressDialog(ClienteAdminActivity.this);
        mProgressDialog.setMessage(getString(R.string.message_processando));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    //Faz Dismiss na ProgressDialog
    public void dismissWait(){
        mProgressDialog.dismiss();
    }
}
