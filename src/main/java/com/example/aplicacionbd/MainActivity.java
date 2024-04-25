package com.example.aplicacionbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    EditText txtNombre, txtDireccion, txtTelefono;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtNombre);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString();
                String direccion = txtDireccion.getText().toString();
                String telefono = txtTelefono.getText().toString();

                ConnectSQLServer connectSQLServer = new ConnectSQLServer();
                try {
                    connectSQLServer.insertData(nombre, direccion, telefono);
                    Toast.makeText(MainActivity.this, "Datos enviados", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error al enviar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class ConnectSQLServer {
        Connection connection;

        public Connection ConnectionHelper() {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.235.242.144;database=prueba1;user=sa;password=1234;");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al cargar el driver JDBC: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al establecer la conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return connection;
        }

        public void insertData(String nombre, String direccion, String telefono) throws SQLException {
            Connection con = ConnectionHelper();
            if (con == null) {
                throw new SQLException("No se pudo establecer la conexión a la base de datos.");
            }
            String query = "INSERT INTO Cliente (nombre, direccion, telefono) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, direccion);
            preparedStatement.setString(3, telefono);
            preparedStatement.executeUpdate();
        }
    }
}
