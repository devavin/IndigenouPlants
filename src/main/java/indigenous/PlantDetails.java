package indigenous;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;


public class PlantDetails extends JFrame{

public static final String PLACES_NS = "http://www.semanticweb.org/vinaybabudevarapalli/ontologies/2019/10/IndigenousPlants#";
    
	public static final String SOURCE = "./src/main/resources/data/";
	
	private JLabel plantName = new JLabel("...");
	private JLabel plantDescription = new JLabel("...");
	private JLabel familyGroup = new JLabel("...");
	private JLabel medicinalParts = new JLabel("...");
	private JLabel chemicalConstituents = new JLabel("...");
	private JLabel medicinalProperties = new JLabel("...");
	private JLabel medicinalUses = new JLabel("...");
	private JLabel taste = new JLabel("...");
	private JPanel contentPane;
	/****************** Define ScrollPane that will load JTable in it ****************************/
	private JScrollPane sp=new JScrollPane();
    public PlantDetails(String name) {
       // super(new GridLayout(1, 1));
         
       
		String country = name;
		//create instance of OntModel class
		OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
		
		String prefix = "prefix plants: <" + PLACES_NS + ">\n" +
                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
                		"prefix owl: <" + OWL.getURI() + ">\n";
		String query_text=  prefix +
				"SELECT ?cname "
				+ "?ccname "
				+ "?family1 "
				+ "(GROUP_CONCAT(DISTINCT str(?part1);separator=';') AS ?part2)  "
				+ "(GROUP_CONCAT(DISTINCT str(?prop1);separator=';') AS ?prop2)  "
				+ "(GROUP_CONCAT(DISTINCT str(?taste1);separator=';') AS ?taste2) "
				+ "(GROUP_CONCAT(DISTINCT str(?chemical1);separator=';') AS ?chemical2) "
				+ "(GROUP_CONCAT(DISTINCT str(?illness1);separator='; ') AS ?illness2) \r\n"  
				+ "	WHERE {?plant a plants:Plants. ?plant plants:plant_name ?cname. ?plant plants:plant_description ?ccname. " ;
		
		    query_text += "?plant plants:hasA ?part. ?part plants:name ?part1. ";
		    query_text += "?plant plants:belongsTo ?family. ?family plants:name ?family1. ";
		    query_text += "?plant plants:contains ?chemical. ?chemical plants:name ?chemical1. ";
		    query_text += "?plant plants:cures ?illness. ?illness plants:name ?illness1. ";
		    query_text += "?plant plants:hasMedicalProperties ?prop. ?prop plants:name ?prop1. ";
		    query_text += "?plant plants:tastes ?taste. ?taste plants:name ?taste1. \r\n"; 
			query_text += "FILTER(regex(str(?cname),\""+country+"\",\"i\")) } ";
			query_text += "GROUP BY ?cname ?ccname ?family1";
		
		
		System.out.println(query_text);
		
		Query query = QueryFactory.create( query_text );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
		
    
        try {
            ResultSet results = qexec.execSelect();
            int i = 0;
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();
               
                /****************************  Assign query data to array. That will populate JTable **************************/
               // values.add(new String[] {qs.get("cname").toString(), qs.get("ccname").toString()});
               /**************************************************************************************************************/
                
                plantName.setText(qs.get("cname").toString());
                plantDescription.setText("<html>"+qs.get("ccname").toString()+"</html>");
                chemicalConstituents.setText(qs.get("chemical2").toString());
                familyGroup.setText(qs.get("family1").toString());
                medicinalUses.setText("<html>"+qs.get("illness2").toString()+"</html>");
                taste.setText(qs.get("taste2").toString());
                medicinalProperties.setText(qs.get("prop2").toString());
                medicinalParts.setText(qs.get("part2").toString()); 
                
               // System.out.println(qs.get("illness2").toString());
                i++;
            }
            
     }
      
        finally {
            qexec.close();
        }
        
        setFont(new Font("Arial", Font.PLAIN, 14));
		setTitle("Indigenous Plants");
		setPreferredSize(new Dimension(600, 600));
		setMaximumSize(new Dimension(700, 700));
		setBounds(100, 100, 700, 700);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(700, 700));
		contentPane.setMaximumSize(new Dimension(400, 400));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
        
        contentPane.setLayout(null);
		
		Panel panel = new Panel();
		panel.setBounds(10, 0, 700, 700);
	    contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel plant_name = new JLabel("<html><b>Plant Name</b></html>");
		plant_name.setBounds(0, 0, 140, 26);
		panel.add(plant_name);
		plantName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		plantName.setBounds(10, 15, 150, 30);
		panel.add(plantName);
		
		JLabel plant_desc = new JLabel("<html><b>Taxonomic Description</b></html>");
		plant_desc.setBounds(0, 45, 200, 26);
		panel.add(plant_desc);
		plantDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
		plantDescription.setBounds(10, 60, 600, 80);
		panel.add(plantDescription);

		
		JLabel medp = new JLabel("<html><b>Medicinal Parts</b></html>");
		medp.setBounds(0, 165, 200, 26);
		panel.add(medp);
		medicinalParts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		medicinalParts.setBounds(10, 180, 600, 30);
		panel.add(medicinalParts); 
		
		JLabel tas = new JLabel("<html><b>Taste</b></html>");
		tas.setBounds(0, 210, 200, 26);
		panel.add(tas);
		taste.setFont(new Font("Tahoma", Font.PLAIN, 14));
		taste.setBounds(10, 225, 600, 30);
		panel.add(taste); 
		
		JLabel chem = new JLabel("<html><b>Chemical Constituents</b></html>");
		chem.setBounds(0, 255, 200, 26);
		panel.add(chem);
		chemicalConstituents.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chemicalConstituents.setBounds(10, 270, 600, 30);
		panel.add(chemicalConstituents); 
		
		JLabel medpr = new JLabel("<html><b>Medicinal Properties</b></html>");
		medpr.setBounds(0, 300, 200, 26);
		panel.add(medpr);
		medicinalProperties.setFont(new Font("Tahoma", Font.PLAIN, 14));
		medicinalProperties.setBounds(10, 315, 600, 30);
		panel.add(medicinalProperties); 
		
		JLabel medu = new JLabel("<html><b>Medicinal Uses</b></html>");
		medu.setBounds(0, 345, 200, 26);
		panel.add(medu);
		medicinalUses.setFont(new Font("Tahoma", Font.PLAIN, 14));
		medicinalUses.setBounds(10, 360, 600, 60);
		panel.add(medicinalUses); 
		
		JLabel famg = new JLabel("<html><b>Family Group</b></html>");
		famg.setBounds(0, 410, 200, 26);
		panel.add(famg);
		familyGroup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		familyGroup.setBounds(10, 425, 600, 30);
		panel.add(familyGroup); 
		
		}


    public static PlantDetails plant_frame = new PlantDetails("");  

	public static void main(String[] args) {
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					plant_frame.setVisible(true);
					plant_frame.setPreferredSize(new Dimension(600,600));
					plant_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
