
public class Flete extends Transporte{
	
	private int acomp;
	private double costoPorAcom;
	
	
	Flete(String idTransp, double cargaMax, double capacidad, double costoKm, int acomp, double costoPorAcom){
		super(idTransp, cargaMax, capacidad, false, costoKm);
		setAcomp(acomp);
		setCostoPorAcom(costoPorAcom);
	}
	
	
	private void setAcomp(int acomp) {
		if(acomp > 0)
			this.acomp = acomp;
		else
			throw new RuntimeException("La cantidad de acompa�antes debe ser mayor a 0");
	}

	private void setCostoPorAcom(double costoPorAcom) {
		if(costoPorAcom > 0)
			this.costoPorAcom = costoPorAcom;
		else
			throw new RuntimeException("El costo de carga debe ser mayor a 0");
	}
	
	public double obtenerCostoViaje() {
		return super.obtenerCostoViaje() + (acomp*costoPorAcom);
	}
	
	public boolean equals(Object o) {
		if(o instanceof Flete)
			return super.equals(o);
		return false;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Transporte: Flete\n");
		cad.append(super.toString());
		cad.append("\nCosto por acompa�ante: $");
		cad.append(costoPorAcom);
		cad.append("\nCantidad de acompa�antes: ");
		cad.append(acomp);
		return cad.toString();
	}
}
