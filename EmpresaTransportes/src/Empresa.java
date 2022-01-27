import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Empresa {
	private String cuit;
	private String nombre;
	public HashMap<String,Transporte> flota;
	public ArrayList<Deposito> depositos;
	public HashMap<String,Destino> destinos;
 	
	Empresa(String cuit,String nombre){
		setCuit(cuit);
		setNombre(nombre);
		flota = new HashMap<>();
		depositos = new ArrayList<>();
		destinos = new HashMap<>();
	}
	
	//SETTERS*******************************************************
	
	private void setCuit(String cuit) {
		if(!cuit.isBlank())
			this.cuit = cuit;
		else
			throw new RuntimeException("El CUIT no puede estar vacío");
	}

	private void setNombre(String nombre) {
		if(!nombre.isBlank())
			this.nombre = nombre;
		else
			throw new RuntimeException("El nombre no puede estar vacío");
	}

	//MÉTODOS PRINCIPALES********************************************
	
	public int agregarDeposito(double capacidad,boolean frigorifico,boolean propio) {
		if(!(!propio && frigorifico)){
			depositos.add(new Deposito(capacidad,frigorifico,propio));
			return depositos.size()-1;
		}
		else
			throw new RuntimeException("Esta función no es para depósitos tercerizados con frigorífico");
	}
	
	public int agregarDepTercerizFrio(double capacidad, double costoPorTonelada) {
		depositos.add(new DepositoTercerizadoFrio(capacidad,costoPorTonelada));
		return depositos.size()-1;
	}
	
	public void agregarDestino(String destino, int km) {
		String clave = destino.toLowerCase();
		Destino valor = new Destino(clave, km);
		destinos.put(clave, valor);
	}
	
	public void agregarTrailer(String idTransp, double cargaMax, double capacidad, boolean frigorifico, double costoKm, double segCarga) {
		flota.put(idTransp, new Trailer(idTransp,cargaMax,capacidad,frigorifico,costoKm,segCarga));
	}
	
	public void agregarMegaTrailer(String idTransp, double cargaMax, double capacidad, boolean frigorifico, double costoKm, double segCarga, double costoFijo, double comida) {
		flota.put(idTransp, new MegaTrailer(idTransp,cargaMax,capacidad,frigorifico,costoKm,segCarga,costoFijo,comida));
	}
	
	public void agregarFlete(String idTransp, double cargaMax, double capacidad, double costoKm, int acomp, double costoPorAcom) {
		flota.put(idTransp, new Flete(idTransp,cargaMax,capacidad,costoKm,acomp,costoPorAcom));
	}
	
	public void asignarDestino(String idTransp, String destino) {
		String dest = destino.toLowerCase();
		if(transporteEstaEnFlota(idTransp) && destinoEstaAgregado(dest) && !tieneMercaderia(idTransp)) {
			int km = obtenerDistancia(dest);
			flota.get(idTransp).asignarDestino(dest, km);
		}
		else
			throw new RuntimeException("No se asignó destino");
	}
	
	public boolean incorporarPaquete(String destino, double peso, double volumen, boolean frio) {
		boolean incorporo = false;
		Iterator<Deposito> it = depositos.iterator();
		
		while(!incorporo && it.hasNext()){
			
			Deposito deposito = it.next();
			if(deposito.incorporarPaquete(destino, peso, volumen, frio))
				incorporo = true;
		}
		return incorporo;
	}

	//ACTUALIZADO
	public double cargarTransporte(String idTransp) {
		if(estaEnFlota(idTransp) && !estaEnViaje(idTransp) && tieneDestinoAsignado(idTransp)) {
			Transporte transporte = flota.get(idTransp);
			Iterator<Deposito> it = depositos.iterator();
			
			while(!transporte.estaLleno() && it.hasNext()) {													
				Deposito deposito = it.next();																	
				if(sonDelMismoTipo(transporte, deposito)) {
					while(!transporte.estaLleno() && deposito.tienePaquetes(transporte.getDestino().getDestino(), transporte.cargaDisponible(), transporte.volumenDisponible())) {											
						Paquete paquete = deposito.obtenerPaquete(transporte.getDestino().getDestino(), transporte.cargaDisponible(), transporte.volumenDisponible());
						if(transporte.cargarPaquete(paquete, deposito))											
								deposito.quitarPaquete(paquete);
					}
				}
			}
			return transporte.getVolumenCargado();
		}
		return 0;
	}
	
	public void iniciarViaje(String idTransp) {
			if(estaEnFlota(idTransp) && !estaEnViaje(idTransp) && tieneDestinoAsignado(idTransp) && (tieneMercaderia(idTransp))) {
				flota.get(idTransp).iniciarViaje();							
			}
			else
				throw new RuntimeException("No se pudo iniciar el viaje");
	}
	
	public void finalizarViaje(String idTransp) {
		if(estaEnViaje(idTransp))
			flota.get(idTransp).finalizarViaje();
		else
			throw new RuntimeException("El transporte no está en viaje");
	}
	
	public double obtenerCostoViaje(String idTransp) {
		if(estaEnViaje(idTransp))
			return flota.get(idTransp).obtenerCostoViaje();
		else
			throw new RuntimeException("El transporte no está en viaje");
	}
	
	//ACTUALIZADO
	public String obtenerTransporteIgual(String idTransp) {
		if(estaEnFlota(idTransp)) {								
			
			Transporte transporte = flota.get(idTransp);
			Iterator<Transporte> it = flota.values().iterator();
			String aux = "";
			boolean encontro = false;
		
			while(!encontro && it.hasNext()) {						
				Transporte otro = it.next();
				if(otro != transporte && transporte.equals(otro)) {	  
					aux = otro.getIdTransp();
					encontro = true;
				}
			}
			if(encontro) return aux;
		}
		return null;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Nombre de la empresa: ");
		cad.append(nombre);
		cad.append("\n");
		cad.append("CUIT: ");
		cad.append(cuit);
		cad.append("\n------------------------------------------------\n");
		cad.append("Flota: ");
		cad.append(mostrarFlota());
		cad.append("\n------------------------------------------------\n");
		cad.append("Depósitos: ");
		cad.append(mostrarDepositos());
		cad.append("\n------------------------------------------------\n");
		cad.append("Destinos: ");
		cad.append(mostrarDestinos());
		return cad.toString();
	}
	
	//MÉTODOS AUXILIARES*********************************************
	
	private boolean transporteEstaEnFlota(String idTransp) {
		return flota.containsKey(idTransp);
	}
	
	private boolean destinoEstaAgregado(String destino) {
		return destinos.containsKey(destino.toLowerCase());
	}
	
	private int obtenerDistancia(String destino) {
		return destinos.get(destino).getKm();
	}
	
	private boolean sonDelMismoTipo(Transporte transporte, Deposito deposito) {
		return deposito.isFrigorifico() == transporte.isFrigorifico();
	}
	
	private boolean estaEnFlota(String idTransp) {
		return flota.containsKey(idTransp);
	}
	
	private boolean estaEnViaje(String idTransp) {
		return flota.get(idTransp).isEnViaje();
	}
	
	private boolean tieneDestinoAsignado(String idTransp) {
		return flota.get(idTransp).tieneDestinoAsignado();
	}
	
	private boolean tieneMercaderia(String idTransp) {
		return flota.get(idTransp).tieneCarga();
	}
	
	private String mostrarFlota() {
		
		if(flota.isEmpty())
			return "La flota está vacía";
		else {
			Set<String> ids = flota.keySet();
			StringBuilder cad = new StringBuilder();
			cad.append("\n\n");
			for(String idTransp: ids) {
				cad.append(flota.get(idTransp).toString());
				cad.append("\n\n");
			}
			return cad.toString();	
		}
			
	}
	
	private String mostrarDepositos() {
		if(depositos.isEmpty())
			return "No hay depósitos en la empresa";
		else {
			StringBuilder cad = new StringBuilder();
			cad.append("\n\n");
			for(Deposito deposito: depositos) {
				cad.append(deposito.toString());
				cad.append("\n\n");
			}
			return cad.toString();
		}
	}
	
	private String mostrarDestinos() {
		if(destinos.isEmpty())
			return "No hay destinos agregados";
		else {
			Set<String> dest = destinos.keySet();
			StringBuilder cad = new StringBuilder();
			cad.append("\n\n");
			for(String destino: dest) {
				cad.append(destinos.get(destino).toString());
				cad.append("\n\n");
			}
			return cad.toString();
		}
	}
	
}
