import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import net.proteanit.sql.DbUtils;


public class Employee {

    private JPanel Main;
    private JTextField txtname;
    private JTextField txtsalary;
    private JTextField txtdepartment;
    private JTextField txtemail;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTable table1;
    private JTextField txtsearch;
    private JTextField txtupdate;

    static Connection myconn  ;
    PreparedStatement mystm  ;
    ResultSet myrs  ;


    public static void main(String[] args)  {

        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }


    

    public Employee() {
            connect();
            tables();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname , email , department ;
                String salary;

                empname = txtname.getText();
                salary = txtsalary.getText();
                email = txtemail.getText();
                department = txtdepartment.getText();

                try {
                    String sql = "insert into employees(first_name, email, department, salary)"
                            + " values (?, ?, ?, ?)";

                    mystm =myconn.prepareStatement(sql);

                    mystm.setString(1 ,empname);
                    mystm.setString(2 ,department);
                    mystm.setString(3 ,email);
                    mystm.setString(4 ,salary);





                    mystm.execute();
                    JOptionPane.showMessageDialog(null , "Record Added!!!!!");
                    tables();

                    txtname.setText("");
                    txtsalary.setText("");
                    txtdepartment.setText("");
                    txtemail.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               String empname , salary , email , department ;

               empname = txtname.getText();
               salary = txtsalary.getText();
               email = txtemail.getText();
               department = txtdepartment.getText();
               String id = txtsearch.getText();

               try {
                   String sql = "update employees " +
                           "set first_name=? , email=? , department=? , salary=? " +
                           "where id =? ";
                   mystm = myconn.prepareStatement(sql);
                   mystm.setString(1 , empname);
                   mystm.setString(3,email);
                   mystm.setString(2,department);
                   mystm.setString(4,salary);
                   mystm.setString(5,id);
                     mystm.executeUpdate();
                     tables();

                       JOptionPane.showMessageDialog(null , "Record Updated");
                   txtname.setText("");
                   txtemail.setText("");
                   txtdepartment.setText("");
                   txtsalary.setText("");



               } catch (Exception ex) {
                   throw new RuntimeException(ex);
               }

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtsearch.getText();
                    mystm =myconn.prepareStatement("select * from employees where id =?");
                    mystm.setString(1 , id);
                    myrs =mystm.executeQuery();

                    if (myrs.next()){
                        String name =myrs.getString(2);
                        String email = myrs.getString(4);
                        String department = myrs.getString(3);
                        String salary = myrs.getString(5);

                        txtname.setText(name);
                        txtemail.setText(email);
                        txtdepartment.setText(department);
                        txtsalary.setText(salary);

                    }else {
                        txtname.setText("");
                        txtemail.setText("");
                        txtdepartment.setText("");
                        txtsalary.setText("");
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtsearch.getText();

                try {
                    String sql = "delete from employees where id =?";
                    mystm = myconn.prepareStatement(sql);
                    mystm.setString(1,id);
                    mystm.execute();
                    JOptionPane.showMessageDialog(null,"Record Deleted");
                    tables();

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    void tables(){
        try {
            mystm =myconn.prepareStatement("select * from employees");
            myrs =mystm.executeQuery();

            table1.setModel(DbUtils.resultSetToTableModel(myrs));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void connect(){
        try {
            myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo" , "student" ,"12345");
            System.out.println("success");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
