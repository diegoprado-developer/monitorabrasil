package com.diegoprado.monitorabrasil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProposicoesAdapter

    //volley
    lateinit var requestQueue: RequestQueue

    var list: MutableList<Proposicao> = emptyList<Proposicao>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        recyclerView = findViewById(R.id.rv_proposicoes)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ProposicoesAdapter(list, this)
        recyclerView.adapter = adapter

        load()
    }

    private fun load(){
        // metodo para acessar a requição Json
        // Method.GET - a consulta / busca , podemos mudar para PATH/POST/PUT ou qualquer outra coisa que precisamos
        // url de acesso
        // null - caso fosse enviar algum dado json para o servidor

        val request = JsonObjectRequest(
            Request.Method.GET,
                "https://dadosabertos.camara.leg.br/api/v2/proposicoes?ordem=ASC&ordenarPor=id",
            null,
            // esperando a resposta
            Response.Listener<JSONObject> {

                list.clear()
                val dados = it.getJSONArray("dados")
                // foreach do 0 até o maximo da lista
                // until = até
                for (i in 0 until dados.length()){
                    val item = dados.getJSONObject(i)
                    list.add(Proposicao(item.getInt("id"),
                        item.getString("uri"),
                        item.getString("siglaTipo"),
                        item.getInt("ano"),
                        item.getString("ementa")))
                }

                adapter.notifyDataSetChanged()
            },
            // caso fosse tratar algum erro
            null
        )
        //criando a lista
        requestQueue.add(request)
    }
}
