package com.example.asynctaskwithapiexample;

    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.asynctaskwithapiexample.utilities.ApiDataReader;
    import com.example.asynctaskwithapiexample.utilities.AsyncDataLoader;
    import com.example.asynctaskwithapiexample.utilities.Constants;

    import java.io.IOException;
    import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    private TextView tvStatus;
    private ArrayAdapter listAdapter;
    private Switch swUseAsyncTask;
    private Spinner spinnerCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lv_items);
        this.tvStatus = findViewById(R.id.tv_status);
        this.swUseAsyncTask = findViewById(R.id.sw_use_async_task);
        this.spinnerCurrency = findViewById(R.id.spinner_currency);
        this.listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvItems.setAdapter(this.listAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCurrency.setAdapter(adapter);
    }

    public void onBtnGetDataClick(View view) {
        this.tvStatus.setText(R.string.loading_data);
        if(this.swUseAsyncTask.isChecked()){
            getDataByAsyncTask();
            Toast.makeText(this, R.string.msg_using_async_task, Toast.LENGTH_LONG).show();
        }
        else{
            getDataByThread();
            Toast.makeText(this, R.string.msg_using_thread, Toast.LENGTH_LONG).show();
        }
    }

    public void getDataByAsyncTask() {
        // Currency selected by the user
        final String selectedCurrency = spinnerCurrency.getSelectedItem().toString();
        new AsyncDataLoader() {
            @Override
            public void onPostExecute(String result) {
                // Result filtering based on the selected currency
                String filteredResult = filterResultByCurrency(result, selectedCurrency);
                tvStatus.setText(getString(R.string.data_loaded) + filteredResult);
            }
        }.execute(Constants.FLOATRATES_API_URL);
    }


    public void getDataByThread() {
        this.tvStatus.setText(R.string.loading_data);
        Runnable getDataAndDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = ApiDataReader.getValuesFromApi(Constants.FLOATRATES_API_URL);
                    final String selectedCurrency = spinnerCurrency.getSelectedItem().toString();

                    String filteredResult = filterResultByCurrency(result, selectedCurrency);

                    Runnable updateUIRunnable = new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText(getString(R.string.data_loaded) + filteredResult);
                        }
                    };
                    runOnUiThread(updateUIRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(getDataAndDisplayRunnable);
        thread.start();
    }

    // Method that filters the result (based on the currency selected by the user)
    private String filterResultByCurrency(String result, String selectedCurrency) {
        String[] lines = result.split("\n");

        StringBuilder filteredResult = new StringBuilder();
        for (String line : lines) {
            if (line.contains(selectedCurrency)) {
                filteredResult.append(line).append("\n");
            }
        }

        return filteredResult.toString();
    }

}