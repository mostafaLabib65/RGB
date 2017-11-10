package paint.eg.edu.alexu.csd.oop.draw.cs62_67.model;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import paint.eg.edu.alexu.csd.oop.draw.Shape;

public class Json {

	private static BufferedWriter bw = null;
	private static BufferedReader br = null;
	
	public static void save(String path, ArrayList<Shape> shapes){
		try {
			bw = new BufferedWriter(new FileWriter(path.toString()));
			bw.write("{\n");
			bw.write("\"shapes\": [\n");
			for(Shape shape : shapes){
				writeProperties(shape);
			}
			bw.write("]\n");
			bw.write("}");
			bw.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void load(String path, ArrayList<Shape> shapes){
		try{
			ShapeFactory factory = new ShapeFactory();
			Point position = new Point();
			Map<String,Double> properties = new HashMap<String,Double>();
			br = new BufferedReader(new FileReader(path));
			String currentLine = new String();
			for(int i = 0; i < 2; i++){
				br.readLine();
			}
			while(!((currentLine = br.readLine()).equals("]"))){
				Shape shape = factory.createShape(getValue(br.readLine()));
				shape.setColor(new Color(Integer.parseInt(getValue(br.readLine()))));
				shape.setFillColor(new Color(Integer.parseInt(getValue(br.readLine()))));
				Double positionX = Double.parseDouble(getValue(br.readLine()));
				Double positionY = Double.parseDouble(getValue(br.readLine()));
				position.setLocation(positionX, positionY);
				shape.setPosition(position);
				String property = br.readLine();
				while(true){
					property = br.readLine();
					if((property.equals("}"))) {
						break;
					}
					String key = getKey(property);
					String value = getValue(property);
					properties.put(key, Double.parseDouble(value));
				}
				shape.setProperties(properties);
				shapes.add(shape);
				br.readLine();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static void writeProperties(Shape shape) throws IOException{
		bw.write("{\n");
		writeKeyValue("Kind",kindOfShape(shape));
		bw.write(",\n");
		writeKeyValue("Color",String.valueOf(shape.getColor().getRGB()));
		bw.write(",\n");
		writeKeyValue("FillColor",String.valueOf(shape.getFillColor().getRGB()));
		bw.write(",\n");
		writeKeyValue("PositionX",String.valueOf(shape.getPosition().getX()));
		bw.write(",\n");
		writeKeyValue("PositionY",String.valueOf(shape.getPosition().getY()));
		bw.write(",\n");
		bw.write("\"properties\": {\n");
		Map<String,Double> properties = shape.getProperties();
		Set<String> keys = properties.keySet();
		int counter = 0;
		for(String key : keys){
			counter++;
			writeKeyValue(key, "" + properties.get(key));
			if(counter != keys.size()){
				bw.write(",\n");
			}
		}
		bw.write("\n");
		bw.write("}\n");
		bw.write("}\n");
	}
	
	private static void writeKeyValue(String key,String value) throws IOException{
		bw.write("\"" + key + "\":" + "\"" + value + "\"");
	}
	
	private static String kindOfShape(Shape shape){
		if (shape instanceof Circle) {
			return "Circle";
		} else if (shape instanceof Ellipse) {
			return "Ellipse";
		} else if (shape instanceof Square) {
			return "Square";
		} else if (shape instanceof Rectangle) {
			return "Rectangle";
		} else if (shape instanceof LineSegment) {
			return "LineSegment";
		} else if (shape instanceof Triangle) {
			return "Triangle";
		} else {
			return null;
		}
	}
	
	private static String getValue(String string){
		String[] splited = string.split(":");
		String value = splited[1];
		if(value.charAt(value.length()-1) == ','){
			value = value.substring(1, value.length()-2);
		}
		else{
			value = value.substring(1, value.length()-1);
		}
		return value;
	}
	
	private static String getKey(String string){
		String[] splited = string.split(":");
		String value = splited[0];
		value = value.substring(1, value.length()-2);
		return value;
	}
}