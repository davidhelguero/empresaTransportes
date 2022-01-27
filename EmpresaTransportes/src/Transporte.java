import java.util.ArrayList;
import java.util.Iterator;

public abstract class Transporte {
	private String idTransp;  
	private double cargaMax;
	private double capacidad;
	private boolean frigorifico;
	private double costoKm;
	private Destino destino;
	private ArrayList<Paquete> paquetes;
	
	//VARIABLES AUXILIARES
	private boolean enViaje;
	private double pesoCargado;	
	private double volumenCargado;
	private double costoPorDepTercFrio;
	
	Transporte(String idTransp,double cargaMax,double capacidad,boolean frigorifico,double costoKm){
		setIdTransp(idTransp);
		setCargaMax(cargaMax);
		setCapacidad(capacidad);
		this.frigorifico = frigorifico;
		setCostoKm(costoKm);
		paquetes = new ArrayList<>();
		enViaje = false;
		pesoCargado = 0;
		volumenCargado = 0;
		costoPorDepTercFrio = 0;
	}
	
	//SETTERS & GETTERS*******************************************************
	
	private void setIdTransp(String idTransp) {
		if(!idTransp.isBlank()) 
			this.idTransp = idTransp;
		else 
			throw new RuntimeException("La identificación no puede estar vacía");
	}
	
	private void setCargaMax(double cargaMax) {
		if(cargaMax > 0)
			this.cargaMax = cargaMax;
		else
			throw new RuntimeException("La carga máxima debe ser mayor a 0");
	}
	
	private void setCapacidad(double capacidad) {
		if(capacidad>0)
			this.capacidad = capacidad;
		else
			throw new RuntimeException("La capacidad debe ser mayor a 0");
	}
	
	private void setCostoKm(double costoKm) {
		if(costoKm > 0) 
			this.costoKm = costoKm;
		else 
			throw new RuntimeException("El costo debe ser mayor a 0");
	}
	
	public boolean isEnViaje() {
		return enViaje;
	}
	
	public boolean isFrigorifico() {
		return frigorifico;
	}
	
	public double getVolumenCargado() {
		return volumenCargado;
	}
	
	public Destino getDestino() {
		return destino;
	}
	
	public String getIdTransp() {
		return idTransp;
	}
	
	private ArrayList<Paquete> getPaquetes() {
		return paquetes;
	}
	
	//MÉTODOS PRINCIPALES*************************************************************
		
	public void asignarDestino(String destino, int km) {
		this.destino = new Destino(destino, km);
	}
	
	public void iniciarViaje() {
		enViaje = true;
	}
	
	public void finalizarViaje() {
		enViaje = false;
		blanquearDestino();
		vaciarCarga();
	}
	
	public boolean tieneDestinoAsignado() {
		return destino != null;
	}
	
	public boolean estaLleno() {
		return pesoCargado == cargaMax || volumenCargado == capacidad;
	}
	
	public boolean cargarPaquete(Paquete paquete, Deposito deposito) {
		if(sePuedeCargar(paquete)) {	
			paquetes.add(paquete);
			aumentarPesoYVolumenCargado(paquete);
			verificarDepositoCosto(paquete, deposito);
			return true;
		}
		return false;
	}
	
	public double obtenerCostoViaje() {
		return costoPorDepTercFrio + costoKm * destino.getKm();
	}
	
	public boolean tieneCarga() {
		return pesoCargado > 0;
	}
	
	public double cargaDisponible() {
		return cargaMax - pesoCargado;
	}
	
	public double volumenDisponible() {
		return capacidad - volumenCargado;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Transporte) {
			if(mismoDestino((Transporte)o) && mismaCarga((Transporte)o))
				return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Identificación: ");
		cad.append(idTransp);
		cad.append("\nCarga máxima: ");
		cad.append(cargaMax);
		cad.append("\nCapacidad: ");
		cad.append(capacidad);
		cad.append("\nFrigorífico: ");
		cad.append(frigorifico);
		cad.append("\nPeso cargado: ");
		cad.append(pesoCargado);
		cad.append("\nVolumen cargado: ");
		cad.append(volumenCargado);
		cad.append("\n");
		if(destino!=null)
			cad.append(destino).toString();
		else
			cad.append("Destino: No asignado");
		cad.append("\nEn viaje: ");
		cad.append(enViaje);
		cad.append("\nCosto por km: $");
		cad.append(costoKm);
		return cad.toString();
		}
	
	//MÉTODOS AUXILIARES**********************************************************
	
	private boolean sePuedeCargar(Paquete paquete) {
		return paquete.getDestino().equals(destino.getDestino())
			&& paquete.isFrio() == frigorifico 
			&& paquete.getPeso() <= (cargaMax - pesoCargado)
			&& paquete.getVolumen() <= (capacidad - volumenCargado);
	}
	
	private void aumentarPesoYVolumenCargado(Paquete paquete) {
		pesoCargado = pesoCargado + paquete.getPeso();
		volumenCargado = volumenCargado + paquete.getVolumen();
	}
	
	private void blanquearDestino() {
		destino = null;
	}
	
	private void vaciarCarga() {
		paquetes.clear();
		pesoCargado = volumenCargado = costoPorDepTercFrio = 0;
	}
	
	//DECISIÓN DE IMPLEMENTACIÓN: CALCULAR EL PROPORCIONAL
	private void verificarDepositoCosto(Paquete paquete, Deposito deposito) {
		if(deposito instanceof DepositoTercerizadoFrio)
			costoPorDepTercFrio += (paquete.getPeso() * ((DepositoTercerizadoFrio)deposito).getCostoPorTonelada()) / 1000;
	}
		
	
	private boolean mismoDestino(Transporte transporte) {
		return transporte.getDestino().getDestino().equals(destino.getDestino());
	}
	
	private boolean mismaCarga(Transporte transporte) {
		
		if(paquetes.size() != transporte.getPaquetes().size()) return false;
		
		ArrayList<Paquete> paquetes2 = new ArrayList<>();
		for(Paquete paquete: transporte.getPaquetes())		//m
			paquetes2.add(paquete);
		
		Iterator<Paquete> it = paquetes.iterator();						
		boolean iguales = true;
		while(iguales && it.hasNext()) {                   //m2 (sumatoria de Gauss)
			Paquete paquete = it.next();
			if(!esta(paquete, paquetes2))
				iguales = false;
		}
		return iguales;
	}
		
	private boolean esta(Paquete paquete, ArrayList<Paquete> paquetes) {
		
		Iterator<Paquete> it = paquetes.iterator();							
		boolean encontro = false;
		
		while(!encontro && it.hasNext()) {
			Paquete p = it.next();
			if(paquete.equals(p)) {
				it.remove();
				encontro = true;
			}
		}
		return encontro;
	}
		
}
