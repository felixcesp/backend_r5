/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tutoria.Servicios;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import tutoria.Modelo.Reservacion;
import tutoria.Repositorio.ReservacionRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tutoria.reportes.ContadorClientes;
import tutoria.reportes.StatusReservas;

/**
 * clase para reservaciones
 * @author FELIX CESPEDES
 */
@Service
public class ServiciosReservacion {
   /**
    * autoincremento
    */
       @Autowired
   /*
     metodos crud  
   */
    private ReservacionRepositorio metodosCrud;
   /**
    * get
    * @return 
    */
    public List<Reservacion> getAll(){
        return metodosCrud.getAll();
    }
    /**
     * id
     * @param reservationId
     * @return 
     */
    public Optional<Reservacion> getReservation(int reservationId) {
        return metodosCrud.getReservation(reservationId);
    }
    /**
     * null
     * @param reservation
     * @return 
     */
    public Reservacion save(Reservacion reservation){
        /**
         * condiconales
         */
        if(reservation.getIdReservation()==null){
            return metodosCrud.save(reservation);
        }else{
            Optional<Reservacion> e= metodosCrud.getReservation(reservation.getIdReservation());
            if(e.isEmpty()){
                return metodosCrud.save(reservation);
            }else{
                return reservation;
            }
        }
    }
    /**
     * metodo put
     * @param reservacion
     * @return 
     */ 
    public Reservacion update(Reservacion reservacion){
        /**
         * condicionales
         */
        if(reservacion.getIdReservation()!=null){
            Optional<Reservacion> e= metodosCrud.getReservation(reservacion.getIdReservation());
            if(!e.isEmpty()){

                if(reservacion.getStartDate()!=null){
                    e.get().setStartDate(reservacion.getStartDate());
                }
                if(reservacion.getDevolutionDate()!=null){
                    e.get().setDevolutionDate(reservacion.getDevolutionDate());
                }
                if(reservacion.getStatus()!=null){
                    e.get().setStatus(reservacion.getStatus());
                }
                metodosCrud.save(e.get());
                return e.get();
            }else{
                return reservacion;
            }
        }else{
            return reservacion;
        }
    }
    /**
     * metodo delete
     * @metodo param reservationId
     * @return 
     */
    public boolean deleteReservation(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservation -> {
            metodosCrud.delete(reservation);
            return true;
        }).orElse(false);
        return aBoolean;
    }
  /**
   * reporte
   * @return 
   */
    public StatusReservas reporteStatusServicio (){
        List<Reservacion>completed= metodosCrud.ReservacionStatusRepositorio("completed");
        List<Reservacion>cancelled= metodosCrud.ReservacionStatusRepositorio("cancelled");
        
        return new StatusReservas(completed.size(), cancelled.size() );
    }
    /**
     * reportes
     * @param datoA
     * @param datoB
     * @return 
     */
    public List<Reservacion> reporteTiempoServicio (String datoA, String datoB){
        /**
         * fecha
         */
        SimpleDateFormat parser = new SimpleDateFormat ("yyyy-MM-dd");
        /**
         *objeto 
         */
        Date datoUno = new Date();
        Date datoDos = new Date();
        
        try{
             datoUno = parser.parse(datoA);
             datoDos = parser.parse(datoB);
        }catch(ParseException evt){
            evt.printStackTrace();
        }if(datoUno.before(datoDos)){
            return metodosCrud.ReservacionTiempoRepositorio(datoUno, datoDos);
        }else{
            return new ArrayList<>();      
        } 
    } 
    /**
     * contador de clientes
     * @return 
     */
     public List<ContadorClientes> reporteClientesServicio(){
            return metodosCrud.getClientesRepositorio();
        } 
}
