import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Deposito {
	private double capacidad;
	private boolean frigorifico;
	private boolean propio;
	private HashMap<String,ArrayList<Paquete>> paquetes;
	
	//VARIABLE AUXILIAR
	private double espacioSuficiente;
	
	Deposito(double capacidad, boolean frigorifico, boolean propio){
		setCapacidad(capacidad);
		this.frigorifico = frigorifico;
		this.propio = propio;
		paquetes = new HashMap<>();
		espacioSuficiente = capacidad;
	}
	
	//SETTERS & GETTERS*********************************************************************
	
	private void setCapacidad(double capacidad) {
		
		if(capacidad>0)
			this.capacidad = capacidad;
		else
			throw new RuntimeException("La capacidad del depósito debe ser mayor a 0");
	}
	
	public boolean isFrigorifico() {
		return frigorifico;
	}
	
	//MÉTODOS PRINCIPALES*********************************************************************
	
	public boolean incorporarPaquete(String destino, double peso, double volumen, boolean frio) {
		
		if(coinciden(frio) && hayEspacioSuficiente(volumen)) {
			
			destino = destino.toLowerCase();
			Paquete paquete = new Paquete(destino, peso, volumen, frio);
			incorporarPaqueteSegunDestino(destino, paquete);
			reducirEspacioSuficiente(paquete);                             
			return true;
		}
		return false;
	}
	
	public boolean tienePaquetes(String destino, double carga, double capacidad) {
		if(obtenerPaquete(destino, carga, capacidad) != null)
			return true;
		return false;
	}
	
	public Paquete obtenerPaquete(String destino, double carga, double capacidad) {
		Paquete aux = null;
		if(estaDestino(destino)) {
			Iterator<Paquete> it = paquetes.get(destino).iterator();
			while(aux == null && it.hasNext()) {
				Paquete paquete = it.next();
				if(paquete.getPeso() <= carga && paquete.getVolumen() <= capacidad)
					aux = paquete;
			}
		}
		return aux;
	}
	
	public void quitarPaquete(Paquete paquete) {
		if(estaPaquete(paquete)) {
			paquetes.get(paquete.getDestino()).remove(paquete);
			aumentarEspacioSuficiente(paquete);
		}
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Frigorífico: ");
		cad.append(frigorifico);
		cad.append("\nPropio o tercerizado: ");
		if(propio)
			cad.append("Propio");
		else
			cad.append("Tercerizado");
		cad.append("\nCapacidad: ");
		cad.append(capacidad);
		cad.append("\nEspacio libre: ");
		cad.append(espacioSuficiente);
		return cad.toString();
	}
	
	
	
	//MÉTODOS AUXILIARES***********************************************************************
	
	private boolean coinciden(boolean frioPaquete) {
		return frigorifico==frioPaquete;
	}
		
	private boolean hayEspacioSuficiente(Double volumenPaquete) {
		return espacioSuficiente>0 && espacioSuficiente<=capacidad && espacioSuficiente>=volumenPaquete;
	}
		
	private void incorporarPaqueteSegunDestino(String destino,Paquete paquete) {
		if(estaDestino(destino))
			paquetes.get(destino).add(paquete);
		else {
			ArrayList<Paquete> lista = new ArrayList<>();
			lista.add(paquete);
			paquetes.put(destino, lista);
		}
	}
	
	private boolean estaDestino(String destino) {
		return paquetes.containsKey(destino);
	}
	
	private void reducirEspacioSuficiente(Paquete paquete) {
		espacioSuficiente -= paquete.getVolumen();
	}
	
	private boolean estaPaquete(Paquete paquete) {
		if(estaDestino(paquete.getDestino()))
			return paquetes.get(paquete.getDestino()).contains(paquete);
		return false;
	}
	
	private void aumentarEspacioSuficiente(Paquete paquete) {
		if(!paquetes.get(paquete.getDestino()).contains(paquete))
			espacioSuficiente+=paquete.getVolumen();
	}
}
