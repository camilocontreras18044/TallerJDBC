import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EstudianteCRUD {
static final String URL = "jdbc:mysql://localhost:3307/jdbc"; 
    static final String USER = "root";  
    static final String PASS = "";      

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENÚ ESTUDIANTES =====");
            System.out.println("1. Insertar estudiante");
            System.out.println("2. Actualizar estudiante");
            System.out.println("3. Eliminar estudiante");
            System.out.println("4. Consultar todos los estudiantes");
            System.out.println("5. Consultar estudiante por email");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1 -> insertarEstudiante(sc);
                case 2 -> actualizarEstudiante(sc);
                case 3 -> eliminarEstudiante(sc);
                case 4 -> consultarTodos();
                case 5 -> consultarPorEmail(sc);
                case 6 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        sc.close();
    }

    private static void insertarEstudiante(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Edad: ");
        int edad = sc.nextInt(); sc.nextLine();
        System.out.print("Estado civil (SOLTERO, CASADO, VIUDO, UNION_LIBRE, DIVORCIADO): ");
        String estadoCivil = sc.nextLine();

        String sql = "INSERT INTO estudiante (nombre, apellido, correo, edad, estado_civil) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setInt(4, edad);
            ps.setString(5, estadoCivil);
            ps.executeUpdate();
            System.out.println(" Estudiante insertado correctamente.");
        } catch (SQLException e) {
            System.out.println(" Error al insertar: " + e.getMessage());
        }
    }

    private static void actualizarEstudiante(Scanner sc) {
        System.out.print("Ingrese el correo del estudiante a actualizar: ");
        String correo = sc.nextLine();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Nueva edad: ");
        int edad = sc.nextInt(); sc.nextLine();
        System.out.print("Nuevo estado civil: ");
        String estadoCivil = sc.nextLine();

        String sql = "UPDATE estudiante SET nombre=?, apellido=?, edad=?, estado_civil=? WHERE correo=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setInt(3, edad);
            ps.setString(4, estadoCivil);
            ps.setString(5, correo);
            int filas = ps.executeUpdate();
            if (filas > 0)
                System.out.println(" Estudiante actualizado.");
            else
                System.out.println(" No se encontró estudiante con ese correo.");
        } catch (SQLException e) {
            System.out.println(" Error al actualizar: " + e.getMessage());
        }
    }

    private static void eliminarEstudiante(Scanner sc) {
        System.out.print("Ingrese el correo del estudiante a eliminar: ");
        String correo = sc.nextLine();

        String sql = "DELETE FROM estudiante WHERE correo=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            int filas = ps.executeUpdate();
            if (filas > 0)
                System.out.println(" Estudiante eliminado.");
            else
                System.out.println(" No se encontró estudiante con ese correo.");
        } catch (SQLException e) {
            System.out.println(" Error al eliminar: " + e.getMessage());
        }
    }

    private static void consultarTodos() {
        String sql = "SELECT * FROM estudiante";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %d | %s\n",
                        rs.getInt("id_estudiante"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("estado_civil"));
            }
        } catch (SQLException e) {
            System.out.println(" Error al consultar: " + e.getMessage());
        }
    }

    private static void consultarPorEmail(Scanner sc) {
        System.out.print("Ingrese el correo del estudiante: ");
        String correo = sc.nextLine();

        String sql = "SELECT * FROM estudiante WHERE correo=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %d | %s\n",
                        rs.getInt("id_estudiante"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("estado_civil"));
            } else {
                System.out.println(" No se encontró estudiante con ese correo.");
            }
        } catch (SQLException e) {
            System.out.println(" Error al consultar: " + e.getMessage());
        }
    }
}
