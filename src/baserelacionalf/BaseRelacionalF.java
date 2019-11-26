package baserelacionalf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author Alba
 */
public class BaseRelacionalF {

    public static Connection conn = null;

    private Connection conexion() {
        final String driver = "jdbc:oracle:thin:";
        final String host = "localhost.localdomain";
        final String porto = "1521";
        final String sid = "orcl";
        final String usuario = "hr";
        final String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;

//        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void callStatement(int entrada, int saida) {

        try (Connection conn = this.conexion()) {

            //Preparamos el callable statement y llamamos al procedure alojado en la bd (para eso hay que importarla primero alli)
            CallableStatement cstmt = conn.prepareCall("{call pjavaprocoracle(?, ?)}");

            //Especificamos los parámetros de entrada
            cstmt.setInt(1, entrada);
            cstmt.setInt(2, saida); //es de entrada y salida

            //Registramos los parámetros de salida (OUT o INOUT)
            cstmt.registerOutParameter(2, Types.INTEGER);

            //Ejecutamos CallableStatement, y recibimos cualquier conjunto de resultados o parámetros de salida
            cstmt.execute();

            //Almacenamos el resultado del procedure en una variable y la imprimimos
            int result = cstmt.getInt(2); //se puede imprimir tmb directamente

            System.out.println("Entrada: " + entrada + "\nSaida: " + saida + "\nResultado: " + result);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void main(String[] args) throws SQLException {
        BaseRelacionalF obx = new BaseRelacionalF();
        obx.callStatement(10, 4);
        conn.close();
    }

}
