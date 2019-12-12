package indigenous;


/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

 
/*
 * TabbedPaneDemo.java requires one additional file:
 *   images/middle.gif.
 */
 
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.GraphStoreFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class MainScreen extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PLACES_NS = "http://www.semanticweb.org/vinaybabudevarapalli/ontologies/2019/10/IndigenousPlants#";
    
	public static final String SOURCE = "./src/main/resources/data/";
	JTextField txtByName;
	JTextArea pName,pDescription,pParts,chemicalCons,medicinalProp,medicinalUse,ptaste;
	JComponent panel2,panel1,panel3,panel4,panel5;
	JComboBox<String> petList,menu,menu1,menu2,menu3;
    JTable table;
    //create instance of OntModel class
  	OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
  	 //read ontology model
  	String prefix = "prefix plants: <" + PLACES_NS + ">\n" +
    		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
    		"prefix owl: <" + OWL.getURI() + ">\n";
	/****************** Define ScrollPane that will load JTable in it ****************************/
	private JScrollPane sp=new JScrollPane();
    public MainScreen() {
        super(new GridLayout(1, 1));
         
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/middle.gif");
         
        panel1 = new JPanel() ;
        tabbedPane.addTab("Home", icon, panel1,"Home Screen");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        JLabel home1 = new JLabel();
        home1.setText("<html>Welcome to Indigenous Plants search <br><br> This application helps searching the IndigenousPlants ontology <br>"
        		+ "Below is the explination for how each tab works <br><br>"
        		+ "<b>Search Tab</b><br>"
        		+ "You can select the type of search from the drop down and specify the string you are<br>"
        		+ "searching for in text and click on search button.Results will be displayed in table below.<br>"
        		+ "on click of a row in a table a details screen will open and provide all details of that plant.<br><br><br>"
        		+ "<b>Filter Tab</b><br>"
        		+ "Using this screen you can filter you results by illness,taste of medicen and form of the<br>"
        		+ "medicen i.e. root,seet etc. Dropdowns will be automatically updated with the availabe<br> "
        		+ "results for the selected options. Onclick of filter results will be displayed in table below.<br>"
        		+ "on click of a row in a table a details screen will open and provide all details of that plant.<br><br><br>"
        		+ "<b>Insert Tab</b><br>"
        		+ "This screen will allow you to insert details into ontology. If an object already exists it will<br>"
        		+ "create the properties for the individual of objects doesnt exixt objects will be created.<br><br><br>"
        		+ "<b>Delete Tab</b><br>"
        		+ "This screen will Delete the individuals fromthe ontology.</html>");
        home1.setAlignmentX(LEFT_ALIGNMENT);
        panel1.add(home1);
        
        panel2 = new JPanel() ;
        JLabel filler = new JLabel("Search By:");
        tabbedPane.addTab("Search", icon, panel2,"Search Screen");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        String[] petStrings = { "Plant Name", "Family Group", "Chemical Constituent", "Medicinal Uses", "Taste" };
        
        petList = new JComboBox<String>(petStrings);
        petList.setSelectedIndex(4);
        
        panel2.add(filler);
        panel2.add(petList);
        
        txtByName = new JTextField();
		txtByName.setToolTipText("Enter Name");
		txtByName.setBounds(100, 177, 355, 51);
		panel2.add(txtByName);
		txtByName.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground(SystemColor.controlHighlight);
		btnSearch.setFocusable(false);
		btnSearch.setFocusTraversalKeysEnabled(false);
		btnSearch.setBounds(456, 177, 140, 51);
		btnSearch.setFocusPainted(false);
		btnSearch.addActionListener(this);
		panel2.add(btnSearch);
         
        panel3 = new JPanel();
        tabbedPane.addTab("Filter", icon, panel3,"Filter the search");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        JLabel filler2 = new JLabel("Illness:");
        JLabel filler3 = new JLabel("Taste:");
        JLabel filler4 = new JLabel("MedicinalPart:");
        panel3.add(filler2);
        
         menu = new JComboBox<String>();
         menu1 = new JComboBox<String>();
         menu2 = new JComboBox<String>();
         //String default1 = "All";
         menu1.addItem("All");
         menu.setSelectedItem("All");
         menu2.addItem("All");
         FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
         
        String query_text=  prefix +
		 "SELECT ?name1 \r\n" + 
		   "	WHERE {?name rdfs:subClassOf* plants:MedicinalUse. ?name plants:name ?name1. } " ;
		       Query query = QueryFactory.create( query_text );
		       QueryExecution qexec = QueryExecutionFactory.create( query, m );
		       ResultSet results = qexec.execSelect();
		       menu.addItem("All");
		       while ( results.hasNext() ) {
		       QuerySolution qs = results.next();
		       menu.addItem(qs.get("name1").toString());
		       }
		        panel3.add(menu);
		        menu.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
						
						menu1.removeAllItems();
						System.out.println(menu.getSelectedItem().toString());
					    if (menu.getSelectedItem().toString().equals("All")) {
					    	menu1.addItem("All");	
					    	menu1.setSelectedItem("All");
					    }else {
		 String query_text1 = prefix +
				 "SELECT ?taste \r\n" +
				 "	WHERE {?plant a plants:Plants. ?plant plants:tastes ?name1. ?name1 plants:name ?taste . ?plant plants:cures ?illness \r\n";
		 query_text1 += "FILTER(regex(str(?illness),\""+menu.getSelectedItem().toString()+"\",\"i\")) }";
		 Query query1 = QueryFactory.create( query_text1 );
	       QueryExecution qexec1 = QueryExecutionFactory.create( query1, m );
	       ResultSet results1 = qexec1.execSelect();
	       while ( results1.hasNext() ) {
	    	QuerySolution qs1 = results1.next();
	       menu1.addItem(qs1.get("taste").toString());
	       System.out.println(qs1.get("taste"));}}
	       }});
	       panel3.add(filler3);
	       panel3.add(menu1);
	       menu1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (menu1.getSelectedItem() == null ) return;
					FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
				    menu2.removeAllItems();
				    if (menu1.getSelectedItem().toString().equals("All")) {
				    	menu2.addItem("All");	
				    }else {
	       String query_text2 = prefix +
					 "SELECT ?partname \r\n" +
					 "	WHERE {?plant a plants:Plants. ?plant plants:tastes ?name1. ?name1 plants:name ?taste. ?plant plants:cures ?illness. ?plant plants:hasA ?parts. ?parts plants:name ?partname \r\n";
			 query_text2 += "FILTER(regex(str(?illness),\""+menu.getSelectedItem().toString()+"\",\"i\") && regex(str(?taste),\""+menu1.getSelectedItem().toString()+"\",\"i\")) }";
			 Query query2 = QueryFactory.create( query_text2);
		       QueryExecution qexec2 = QueryExecutionFactory.create( query2, m );
		       ResultSet results2 = qexec2.execSelect();
		       while ( results2.hasNext() ) {
		       QuerySolution qs2 = results2.next();
		       menu2.addItem(qs2.get("partname").toString());
		       System.out.println(qs2.get("partname"));
		       }}}});
		       panel3.add(filler4);
		       panel3.add(menu2);
		       
		       JButton filterSearch = new JButton("Search");
		       filterSearch.setBackground(SystemColor.controlHighlight);
		       filterSearch.setFocusable(false);
		       filterSearch.setFocusTraversalKeysEnabled(false);
		       filterSearch.setBounds(456, 177, 140, 51);
		       filterSearch.setFocusPainted(false);
		       panel3.add(filterSearch);
		       filterSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						String prefix = "prefix plants: <" + PLACES_NS + ">\n" +
		                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
		                		"prefix owl: <" + OWL.getURI() + ">\n";
						String query_text3;
						if (menu.getSelectedItem().toString().equals("All") && menu1.getSelectedItem().toString().equals("All") && menu2.getSelectedItem().toString().equals("All")) {
							query_text3 = prefix +
							 "SELECT ?pname ?pdesc \r\n" +
							 "	WHERE {?plant a plants:Plants. ?plant plants:plant_name ?pname. ?plant plants:plant_description ?pdesc. }";
						}else {
						 query_text3 = prefix +
								 "SELECT ?pname ?pdesc \r\n" +
								 "	WHERE {?plant a plants:Plants. ?plant plants:tastes ?name1. "
								    + "?plant plants:plant_name ?pname. ?plant plants:plant_description ?pdesc. "
								     + "    ?name1 plants:name ?taste. ?plant plants:cures ?illness. ?plant plants:hasA ?parts. \r\n";
						 query_text3 += "FILTER(regex(str(?illness),\""+menu.getSelectedItem().toString()+"\",\"i\") "
						 		        + "&& regex(str(?taste),\""+menu1.getSelectedItem().toString()+"\",\"i\") "
						 		        + "&& regex(str(?parts),\""+menu2.getSelectedItem().toString()+"\",\"i\")) }";}
						 OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
				      	 FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
				        /*************************************** Create Arrays for Table Headers and Table Values **********************************/ 
				        List<String> columns = new ArrayList<String>();
				        List<String[]> values = new ArrayList<String[]>();

				        columns.add("Plat Name");
				        columns.add("Plant Description");
				        /*******************************************************************************************************************************/

						 Query query2 = QueryFactory.create( query_text3);	
						 QueryExecution qexec1 = QueryExecutionFactory.create( query2, m );
						 try {
					            ResultSet results = qexec1.execSelect();
					           // int i = 0;
					            while ( results.hasNext() ) {
					                QuerySolution qs = results.next();
					               
					                /****************************  Assign query data to array. That will populate JTable **************************/
					                values.add(new String[] {qs.get("pname").toString(), qs.get("pdesc").toString()});
					               /**************************************************************************************************************/
					                
					                System.out.println(qs.get("pname"));
					             //   i++;
					            }
					            
					         /*************************Create Table and tableModel******************************/
					            TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
					            table = new JTable(tableModel);
					            
					            table.setForeground(Color.DARK_GRAY);
					            table.setBackground(Color.WHITE);
					            table.setRowHeight(35);		
					            sp.setViewportView(table);		           
					            //sp.setBounds(30, 80, 450, 317);
					            panel3.add(sp);
					            panel3.repaint();
					            table.addMouseListener(new java.awt.event.MouseAdapter()

								            {
								
								public void mouseClicked(java.awt.event.MouseEvent e)
								
								{
								
								int row=table.rowAtPoint(e.getPoint());
								//int col= table.columnAtPoint(e.getPoint());
								//JOptionPane.showMessageDialog(null,"Value is"+table.getValueAt(row,0).toString());
								System.out.println("value is"+table.getValueAt(row,0).toString());
								new PlantDetails(table.getValueAt(row,0).toString()).setVisible(true);
								}});


					          /*********************************************************************************/
					        }
					      
					        finally {
					            qexec1.close();
					        }
					}	
					});
				
		 
		panel4 = new JPanel(); 
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Insert Plant", icon, panel4,"Inserts Plant Details");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
       
        panel4.setLayout(null);
        pName = new JTextArea();
        JLabel plant_name = new JLabel("<html><b>Plant Name:</b></html>");
		panel4.add(plant_name);
		plant_name.setBounds(5, 10, 100, 20);
		pName.setToolTipText("Enter Name");
		pName.setBounds(180, 10, 200, 20);
		panel4.add(pName);
		pName.setColumns(25);
		
		pDescription = new JTextArea(5,20);
        JLabel plant_desc = new JLabel("<html><b>Taxonomic Description:</b></html>");
		panel4.add(plant_desc);
		plant_desc.setBounds(5, 50, 200, 20);
		pDescription.setToolTipText("Enter Description");
		panel4.add(plant_desc);
		pDescription.setBounds(180, 50, 350, 80);
		pDescription.setColumns(25);
		panel4.add(pDescription);
		
		pParts = new JTextArea(5,20);
        JLabel medparts = new JLabel("<html><b>Medicinal Parts:</b></html>");
		panel4.add(medparts);
		medparts.setBounds(5, 150, 200, 20);
		pParts.setToolTipText("Enter Description");
		pParts.setBounds(180, 150, 350, 40);
		pParts.setColumns(25);
		panel4.add(pParts);
		
		ptaste = new JTextArea(5,20);
        JLabel plant_taste = new JLabel("<html><b>Taste:</b></html>");
		panel4.add(plant_taste);
		plant_taste.setBounds(5, 200, 200, 20);
		ptaste.setToolTipText("Enter Description");
		ptaste.setBounds(180, 200, 350, 40);
		ptaste.setColumns(25);
		panel4.add(ptaste);
		
		chemicalCons = new JTextArea(5,20);
        JLabel plant_chem = new JLabel("<html><b>Chemical Constituents:</b></html>");
		panel4.add(plant_chem);
		plant_chem.setBounds(5, 250, 200, 20);
		chemicalCons.setToolTipText("Enter Description");
		chemicalCons.setBounds(180, 250, 350, 40);
		chemicalCons.setColumns(25);
		panel4.add(chemicalCons);
		
		medicinalProp = new JTextArea(5,20);
        JLabel plant_medp = new JLabel("<html><b>Medicinal Properties:</b></html>");
		panel4.add(plant_medp);
		plant_medp.setBounds(5, 300, 200, 20);
		medicinalProp.setToolTipText("Enter Description");
		medicinalProp.setBounds(180, 300, 350, 40);
		medicinalProp.setColumns(25);
		panel4.add(medicinalProp);
		
		medicinalUse = new JTextArea(5,20);
        JLabel plant_medu = new JLabel("<html><b>Medicinal Uses:</b></html>");
		panel4.add(plant_medu);
		plant_medu.setBounds(5, 350, 200, 20);
		medicinalUse.setToolTipText("Enter Description");
		medicinalUse.setBounds(180, 350, 350, 40);
		medicinalUse.setColumns(25);
		panel4.add(medicinalUse);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.setBackground(SystemColor.controlHighlight);
		btnInsert.setFocusable(false);
		btnInsert.setFocusTraversalKeysEnabled(false);
		btnInsert.setBounds(250, 400, 100, 25);
		btnInsert.setFocusPainted(false);
		panel4.add(btnInsert);
		
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				String name = pName.getText().trim();
				pName.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Pattern p = Pattern.compile("[a-zA-Z\\s]*");
				Matcher m = p.matcher(name);
				if(!m.matches() || name.equals("")){
					if(name.equals(""))
						JOptionPane.showMessageDialog(null, "Plant name cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets and spaces allowed in Plant name");
					pName.setBorder(BorderFactory.createLineBorder(Color.RED));
				    return;
				}
				
				String desc =pDescription.getText().trim();
				pDescription.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Pattern p1 = Pattern.compile("[a-zA-Z0-9-.,\\s]*");
				Matcher m1 = p1.matcher(desc);
				if(!m1.matches() || desc.equals("")){
					if(desc.equals(""))
						JOptionPane.showMessageDialog(null, "Taxonomic Desceiption cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces,numbers and . are allowed in Taxonomic Desceiption");
					pDescription.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				
				pParts.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Pattern p2 = Pattern.compile("[a-zA-Z;\\s]*");
				Matcher m2 = p2.matcher(pParts.getText().trim());
				if(!m2.matches() || pParts.getText().trim().equals("")){
					if(pParts.getText().trim().equals(""))
						JOptionPane.showMessageDialog(null, "Medicinal Parts cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces and ; are allowed in Medicinal Parts");
					pParts.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				ptaste.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Matcher m3 = p2.matcher(ptaste.getText().trim());
				if(!m3.matches() || ptaste.getText().trim().equals("")){
					if(ptaste.getText().trim().equals(""))
						JOptionPane.showMessageDialog(null, "Taste cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces and ; are allowed in Taste");
					ptaste.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				chemicalCons.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Pattern p3 = Pattern.compile("[a-zA-Z0-9;\\s-]*");
			    Matcher m4 = p3.matcher(chemicalCons.getText().trim());
				if(!m4.matches() || chemicalCons.getText().trim().equals("")){
					if(chemicalCons.getText().trim().equals(""))
						JOptionPane.showMessageDialog(null, "Chemical Constituents cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces,numbers,- and ; are allowed in Chemical Constituents");
					chemicalCons.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				medicinalProp.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Matcher m5 = p2.matcher(medicinalProp.getText().trim());
				if(!m5.matches() || medicinalProp.getText().trim().equals("")){
					if(medicinalProp.getText().trim().equals(""))
						JOptionPane.showMessageDialog(null, "Medicinal Properties cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces and ; are allowed in Medicinal Properties");
					medicinalProp.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				medicinalUse.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				Matcher m6 = p2.matcher(medicinalUse.getText().trim());
				if(!m6.matches() || medicinalUse.getText().trim().equals("")){
					if(medicinalUse.getText().trim().equals(""))
						JOptionPane.showMessageDialog(null, "Medicinal Uses cannot be empty");
					else	
					JOptionPane.showMessageDialog(null, "Only Alphabets,spaces and ; are allowed in Medicinal Uses");
					medicinalUse.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				
				String prefix = "prefix plants: <" + PLACES_NS + ">\n" +
                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
                		"prefix owl: <" + OWL.getURI() + ">\n" +
                		"prefix rdf: <" + RDF.getURI() + ">\n";   
				
				String  mparts[] = pParts.getText().trim().toLowerCase().split(";");
				int i=0;
				String parts = "";
				while (i < mparts.length) {
					int j = 0;
					String  corcCase[] = mparts[i].split(" ");
				String part ="",names ="";
				
			    if(mparts[i].trim().contains(" "))
			    	while (j < corcCase.length) {
						part = part + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1);
						names = names + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1) + " ";
						j++;
					}
			    else	
				part = mparts[i].trim().substring(0, 1).toUpperCase() + mparts[i].trim().substring(1);
			    if (names.equals(""))
					names = part;
				parts = parts + "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:PlantMedicinalParts. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:hasA plants:"+part+". \n" ;
				i++;}
				
				String  ptastes[] = ptaste.getText().trim().toLowerCase().split(";");
				i=0;
				String tastes = "";
				while (i < ptastes.length) {
				String part ="",names ="";
				if(ptastes[i].trim().contains(" ")) {
			    	String  corcCase[] = ptastes[i].split(" ");
				    int j = 0;
				
			    	while (j < corcCase.length) {
						part = part + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1);
						names = names + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1) + " ";
						j++;
					}}
			    else	
				part = ptastes[i].trim().substring(0, 1).toUpperCase() + ptastes[i].trim().substring(1);
				if (names.equals(""))
					names = part;
				tastes = tastes + "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:Taste. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:tastes plants:"+part+". \n" ;
				i++;}
				
				String  pchem[] = chemicalCons.getText().trim().toLowerCase().split(";");
				i=0;
				String chemcon = "";
				while (i < pchem.length) {
				String part ="",names ="";
				if(pchem[i].trim().contains(" ")) {
					int j = 0;
					String  corcCase[] = pchem[i].split(" ");
				   while (j < corcCase.length) {
						part = part + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1);
						names = names + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1) + " ";
						j++;
					}}
			    else	
				part = pchem[i].trim().substring(0, 1).toUpperCase() + pchem[i].trim().substring(1);
				if (names.equals(""))
					names = part;
			    chemcon = chemcon + "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:ChemicalConstituents. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:contains plants:"+part+". \n" ;
				i++;}
				
				String  mprop[] = medicinalProp.getText().trim().toLowerCase().split(";");
				i=0;
				String medprop = "";
				while (i < mprop.length) {
				String part ="",names ="";
				if(mprop[i].trim().contains(" ")) {
					int j = 0;
					String  corcCase[] = mprop[i].split(" ");
				   while (j < corcCase.length) {
						part = part + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1);
						names = names + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1) + " ";
						j++;
					}}
			    else	
				part = mprop[i].trim().substring(0, 1).toUpperCase() + mprop[i].trim().substring(1);
				if (names.equals(""))
					names = part;
				medprop = medprop + "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:MedicinalProperties. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:hasMedicalProperties plants:"+part+". \n" ;
				i++;}
				
				String  muse[] = medicinalUse.getText().trim().toLowerCase().split(";");
				i=0;
				String meduse = "";
				while (i < muse.length) {
				String part ="",names ="";
				if(muse[i].trim().contains(" ")) {
					int j = 0;
					String  corcCase[] = muse[i].split(" ");
				   while (j < corcCase.length) {
						part = part + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1);
						names = names + corcCase[j].trim().substring(0, 1).toUpperCase() + corcCase[j].trim().substring(1) + " ";
						j++;
					}}
			    else	
				part = muse[i].trim().substring(0, 1).toUpperCase() + muse[i].trim().substring(1);
				if (names.equals(""))
					names = part;
				meduse = meduse + "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:MedicinalUse. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:cures plants:"+part+". \n" ;
				i++;}
				
				String part = "ZINGIBERACEAE" ;
				String names = part;
				String belong = "plants:"+part+" rdf:type owl:Class. plants:"+part+" rdfs:subClassOf plants:FamilyGroup. plants:"+part+" rdf:type owl:NamedIndividual,plants:"+part+". plants:"+part+" plants:name \""+names.trim()+"\". plants:plant2 plants:belongsTo plants:"+part+". \n" ;
				
				String query_text4 = prefix + "INSERT { plants:plant2 rdf:type plants:Plants,owl:NamedIndividual . "
				 		+ "plants:plant2 plants:plant_name \""+name+"\". plants:plant2 plants:plant_description \""+desc+"\". \n"
				 				+ parts + tastes + chemcon + medprop + meduse + belong
				 				+ "} WHERE { FILTER NOT EXISTS { plants:plant2 rdf:type plants:Plants . } } ";
					 
					 System.out.println(query_text4);

					 OntModel mo = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
					 FileManager.get().readModel( mo, SOURCE + "IndigenousPlants.owl" );
				
					 UpdateRequest update = UpdateFactory.create(query_text4);
					 UpdateProcessor processor = UpdateExecutionFactory.create(update, GraphStoreFactory.create(mo));
					 processor.execute();
		                
		                try {
							mo.write(new FileOutputStream(SOURCE +"IndigenousPlants.owl"), "RDF/XML-ABBREV");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			}});
          
		panel5 = new JPanel(); 
        panel5.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Delete Plant", icon, panel5,"Delete Plant Details");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        //panel5.add
        menu3 = new JComboBox<String>();
        String query_text4 = prefix +
				 "SELECT ?pname \r\n" +
				 "	WHERE {?plant a plants:Plants. ?plant plants:plant_name ?pname. }";
           Query query3 = QueryFactory.create( query_text4);
	       QueryExecution qexec3 = QueryExecutionFactory.create( query3, m );
	       ResultSet results3 = qexec3.execSelect();
	       while ( results3.hasNext() ) {
	       QuerySolution qs2 = results3.next();
	       menu3.addItem(qs2.get("pname").toString());
	       }
	       panel5.add(menu3);
	       
	       JButton delPlant = new JButton("Delete");
	       delPlant.setBackground(SystemColor.controlHighlight);
	       delPlant.setFocusable(false);
	       delPlant.setFocusTraversalKeysEnabled(false);
	       delPlant.setBounds(456, 177, 140, 51);
	       delPlant.setFocusPainted(false);
	       panel5.add(delPlant);
	       delPlant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					String name = menu3.getSelectedItem().toString();
					String query_text5 = prefix + "DELETE  "
							+ "WHERE {?plant a plants:Plants. ?plant plants:plant_name \""+name+"\";"
							+ "?property      ?value }";
					 System.out.println(query_text5);

					 OntModel mo = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
					 FileManager.get().readModel( mo, SOURCE + "IndigenousPlants.owl" );
				
					 UpdateRequest update = UpdateFactory.create(query_text5);
					 UpdateProcessor processor = UpdateExecutionFactory.create(update, GraphStoreFactory.create(mo));
					 processor.execute();
		                
		                try {
							mo.write(new FileOutputStream(SOURCE +"IndigenousPlants.owl"), "RDF/XML-ABBREV");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				}});
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
     
    /** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
		String search = txtByName.getText().toString().toLowerCase();
		int searchBy = petList.getSelectedIndex();
		OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		FileManager.get().readModel( m, SOURCE + "IndigenousPlants.owl" );
		
		String prefix = "prefix plants: <" + PLACES_NS + ">\n" +
                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
                		"prefix owl: <" + OWL.getURI() + ">\n";
		if(search != null && !search.isEmpty()) {
		String query_text=  prefix +
				"SELECT ?pname ?pdesc \r\n" + 
				"	WHERE {?plant a plants:Plants. ?plant plants:plant_name ?pname. ?plant plants:plant_description ?pdesc. " ;
		
	     if(searchBy == 0)
			query_text += "FILTER(regex(str(?pname),\""+search+"\",\"i\")) }";
		else if (searchBy == 1){
			query_text += "?plant plants:belongsTo ?family. \r\n";
		    query_text += "FILTER(regex(str(?family),\""+search+"\",\"i\")) }";}
		else if (searchBy == 2){
			query_text += "?plant plants:contains ?chemical. \r\n";
		    query_text += "FILTER(regex(str(?chemical),\""+search+"\",\"i\")) }";}
		else if (searchBy == 3) {
			query_text += "?plant plants:cures ?illness. \r\n";
		    query_text += "FILTER(regex(str(?illness),\""+search+"\",\"i\")) }";}
		else if (searchBy == 4) {
			query_text += "?plant plants:tastes ?taste. \r\n";
		    query_text += "FILTER(regex(str(?taste),\""+search+"\",\"i\")) }";}
		
		System.out.println(query_text);
		
		Query query = QueryFactory.create( query_text );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
		
        /*************************************** Create Arrays for Table Headers and Table Values **********************************/ 
        List<String> columns = new ArrayList<String>();
        List<String[]> values = new ArrayList<String[]>();

        columns.add("Plat Name");
        columns.add("Plant Description");
        /*******************************************************************************************************************************/

        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();
               
                /****************************  Assign query data to array. That will populate JTable **************************/
                values.add(new String[] {qs.get("pname").toString(), qs.get("pdesc").toString()});
               /**************************************************************************************************************/
            }
            
         /*************************Create Table and tableModel******************************/
            TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
            table = new JTable(tableModel);
            
            table.setForeground(Color.DARK_GRAY);
            table.setBackground(Color.WHITE);
            table.setRowHeight(35);		
            sp.setViewportView(table);		           
            panel2.add(sp);
            panel2.repaint();
            table.addMouseListener(new java.awt.event.MouseAdapter() {
			
			public void mouseClicked(java.awt.event.MouseEvent e)
			{
			
			int row=table.rowAtPoint(e.getPoint());
			System.out.println("value is"+table.getValueAt(row,0).toString());
			new PlantDetails(table.getValueAt(row,0).toString()).setVisible(true);
			}});
          /*********************************************************************************/
        }
      
        finally {
            qexec.close();
        }
		}

		
	}

        
    //}
    
  
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
       
        panel.add(filler);
        return panel;
    }
     
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainScreen.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("IndigenousPlants");
        frame.setPreferredSize(new Dimension(650,650));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new MainScreen(), BorderLayout.CENTER);
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        createAndShowGUI();
            }
        });
    }
}

