package io.github.alexlondon07.fruits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.alexlondon07.fruits.adapters.FruitAdapter;
import io.github.alexlondon07.fruits.models.Fruit;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //ListView and GridView Adapter
    private ListView listView;
    private GridView gridView;
    private FruitAdapter adapterListview;
    private FruitAdapter adapterGridView;

    //Listado de nuestras frutas
    private List<Fruit> fruits;

    //Menu de opciones Grid / List
    private MenuItem itemListView;
    private MenuItem itemGridView;

    //Variables
    private int counter = 0;
    private final int SW_TO_LIST_VIEW = 0;
    private final int SW_TO_GRID_VIEW = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mostramos icono
        this.enforcerIconBar();
        
        this.fruits = getAllFruits();

        this.listView =  findViewById(R.id.activity_main_listView);
        this.gridView = findViewById(R.id.activity_main_gridView);

        //Adjuntamos el mismo click para ambos casos
        this.listView.setOnItemClickListener(this);
        this.gridView.setOnItemClickListener(this);


        this.adapterListview = new FruitAdapter(this, R.layout.list_view_item_fruit, fruits);
        this.adapterGridView = new FruitAdapter(this, R.layout.grid_view_item_fruit, fruits);

        this.listView.setAdapter(adapterListview);
        this.gridView.setAdapter(adapterGridView);

        //Registramos el context menu para ambos casos
        registerForContextMenu(this.listView);
        registerForContextMenu(this.gridView);

    }

    private void enforcerIconBar() {
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.clickFruit(fruits.get(position));
    }

    /**
     * Metódo para mostrar un mensaje al pulsar en cualquier item
     * @param fruit
     */
    private void clickFruit(Fruit fruit) {
        //Diferenciamos entre las frutras conocidas y desconocidas
        if(fruit.getOrigin().equals("Unknown")){
            Toast.makeText(this, "Sorry, we don´t have many info about " +fruit.getName(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "The best fruit from " + fruit.getOrigin(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Infamos el opcion menu con nuestro layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        //Despues de inflar, recogemos las referencias a los botones que nos interesen
        this.itemListView = menu.findItem(R.id.list_view);
        this.itemGridView = menu.findItem(R.id.grid_view);
        return true;
    }


    /**
     * Eventos para los click en los botones del opcions menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.add_fruit:
                this.addFruit(new Fruit("Added nº " +(++counter), R.drawable.icons8_donut_48, "Unknown"));
                return true;

            case R.id.list_view:
                this.switchListGridView(this.SW_TO_LIST_VIEW);
                return true;

            case R.id.grid_view:
                this.switchListGridView(this.SW_TO_GRID_VIEW);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflamos el context menu con nuestro layout
        MenuInflater inflater = getMenuInflater();

        // Antes de inflar, le añadimos el header dependiendo del objeto que se halla clickeado
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.fruits.get(info.position).getName());

        // Inflamos
        inflater.inflate(R.menu.context_menu_fruits, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Obtenemos info en el context menu del objeto que se pinche
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_fruit:
                this.deleteFruit(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Método para cambiar entre Grid/List view
     * @param op
     */
    private void switchListGridView(int op) {
        if (op == SW_TO_LIST_VIEW) {
            // Validamos si está el list view en modo invisible...
            if (this.listView.getVisibility() == View.INVISIBLE) {
                showListView();
            }
        } else if (op == SW_TO_GRID_VIEW) {
            // Validamos si está el grid view en modo invisible...
            if (this.gridView.getVisibility() == View.INVISIBLE) {
                showGridView();
            }
        }
    }

    /**
     * Metódo para Mostrar listado de frutas en un Grid view y ocultar el botón de Listview
     */
    private void showGridView() {
        this.listView.setVisibility(View.INVISIBLE);
        this.itemListView.setVisible(true);
        this.gridView.setVisibility(View.VISIBLE);
        this.itemGridView.setVisible(false);
    }

    /**
     * Metódo para Mostrar listado de frutas en un List view y ocultar el botón de GridView
     */
    private void showListView() {
        //Escondemos el grid view, y mostramos su botón en el menú de opciones
        this.gridView.setVisibility(View.INVISIBLE);

        //Ponemos el Botón de Gridview visible
        this.itemGridView.setVisible(true);

        //Se pone list view visible, y escondemos su botón en el menú de opciones
        this.listView.setVisibility(View.VISIBLE);

        //Ponemos el botón del ListView invisible
        this.itemListView.setVisible(false);
    }

    /**
     * Metódo para obtener Listado de frutas
     * @return
     */
    private List<Fruit> getAllFruits() {
        List<Fruit> list  = new ArrayList<Fruit>(){{
            add(new Fruit("Chocolatina", R.drawable.icons8_chocolatina_48, "Colombia"));
            add(new Fruit("Hamburguesa", R.drawable.icons8_hamburguesa_48, "USA"));
            add(new Fruit("Helado", R.drawable.icons8_cucurucho_de_helado_48, "España"));
            add(new Fruit("Papas Fritas", R.drawable.icons8_papas_fritas_48, "Francia"));
            add(new Fruit("Cereza", R.drawable.icons8_cereza_48, "Chile"));
            add(new Fruit("Pizza", R.drawable.icons8_pizza_48, "Colombia"));
            add(new Fruit("Queso", R.drawable.icons8_queso_48, "Italia"));
            add(new Fruit("Frambruesa", R.drawable.icons8_frambuesa_48, "Italia"));
            add(new Fruit("Uvas", R.drawable.icons8_uvas_48, "Chile"));
        }};
        return  list;
    }

    /**
     * Metódo para agregar frutas
     * @param fruit
     */
    private void addFruit(Fruit fruit){
        this.fruits.add(fruit);
        //Notificamos el cambio en ambos adaptadores
        this.adapterListview.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

    /**
     * Metódo para eliminar frutas
     * @param position
     */
    private void deleteFruit(int position){
        this.fruits.remove(position);
        //Notificamos el cambio en ambos adaptadores
        this.adapterListview.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

}
