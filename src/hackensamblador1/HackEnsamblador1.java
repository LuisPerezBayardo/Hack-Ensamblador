package hackensamblador1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.pow;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;




public class HackEnsamblador1 extends JFrame implements ActionListener{
    
    JTextArea TA, debug;
    JButton submit;
    JLabel resultado;
    int[] ram=new int[256];
    
    
    public HackEnsamblador1(){
        submit=new JButton();
        submit.setVisible(true);
        submit.setText("Submit");
        submit.setBounds(210, 700, 100, 25);
        submit.addActionListener(this);
        this.add(submit);
        
        resultado=new JLabel();
        resultado.setVisible(true);
        resultado.setBackground(Color.white);
        resultado.setOpaque(true);
        resultado.setBounds(600, 330, 130, 25);
        this.add(resultado);
        
        TA=new JTextArea();
        TA.setVisible(true);
        TA.setText("ADD 0x01, 0x02");
        TA.setBounds(30, 20, 500, 650);
        this.add(TA);
        
        debug=new JTextArea();
        debug.setVisible(true);
        debug.setBounds(600, 370, 500, 350);
        this.add(debug);
        
        for(int i=0; i<256; i++) ram[i]=0;
        
        this.setBounds(0, 0, 2000, 2000);
        this.setResizable(false);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        HackEnsamblador1 HE=new HackEnsamblador1();
        HE.show();
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Boolean error=false;
        String line, nemonico, data, dir;
        int direccion=0, dato=0;
        String text=TA.getText();
        StringTokenizer tokenLine=new StringTokenizer(text, "\n");
        while(tokenLine.hasMoreElements() && !error){
            line=tokenLine.nextToken();
            StringTokenizer tokenSpace=new StringTokenizer(line, " ");
            nemonico=tokenSpace.nextToken();
            if(tokenSpace.hasMoreElements() && !error){
                dir=tokenSpace.nextToken();
                
                if(("0".equals(""+dir.charAt(0)))  &&  ("x".equals(""+dir.charAt(1)))  &&  (",".equals(""+dir.charAt(4)))  &&  (dir.length()==5)){
                    dir=dir.substring(2, 4);
                    direccion=parseInt(dir);
                    if(tokenSpace.hasMoreElements()){
                        data=tokenSpace.nextToken();
                        if(("0".equals(""+data.charAt(0)))  &&  ("x".equals(""+data.charAt(1)))  &&  (data.length()==4)){
                            data=data.substring(2);
                            dato=parseInt(data);
                        }
                        else if(("#".equals(""+data.charAt(0)))  &&  ("0".equals(""+data.charAt(1)))  &&  ("x".equals(""+data.charAt(2)))  &&  (data.length()==5)){
                            data=data.substring(3);
                            dato=parseInt(data);
                            nemonico="MOV#";
                        }
                        else{
                            resultado.setText("Syntax error 3");
                            error=true;
                        }
                    }
                    else{
                        resultado.setText("Syntax error 2");
                        error=true;
                    }
                }
                else{
                    resultado.setText("Syntax error 0");
                    error=true;
                }
            }
            
            if(!error){
                outWriter("hola\ndireccion: "+direccion+"\ndato: "+dato);
                switch (nemonico) {
                    case "MOV#":
                        ram[direccion]=dato;
                        break;
                    case "MOV":
                        ram[direccion]=ram[dato];
                        break;
                    case "ADD":
                        ram[direccion]=sum(ram[dato], ram[direccion]);
                        break;
                    case "MUL":
                        ram[direccion]=mult(ram[dato], ram[direccion]);
                        break;
                    default:
                        resultado.setText("Sintax error 1");
                        error=true;
                        break;
                }
                resultado.setText(String.valueOf(ram[direccion]));
            }
        }
    }
    
    
    
    int parseInt(String d){
        int r=0, temp;
        for(int i=0; i<2; i++){
            temp=convert(d.charAt(i));
            if((48<=d.charAt(i)) && (57>=d.charAt(i))) r+=((pow(16, 1-i))*(d.charAt(i)-48));
            else if(temp!=0) r+=((pow(16, 1-i))*temp);
        }
        return r;
    }
    
    int convert(char a){
        switch (a) {
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
            default:
                break;
        }
        return 0;
    }
    
    
    
    int sum(int dato1, int dato2){
        return dato1+dato2;
    }
    
    int mult(int dato1, int dato2){
        int res=0;
        for(int i=0; i<dato2; i++) res+=dato1;
        return res;
    }
    
    
    void outWriter(String a){
        String b=debug.getText();
        debug.setText(b+"\n"+a);
    }
}
