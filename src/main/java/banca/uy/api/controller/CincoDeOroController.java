package banca.uy.api.controller;

import banca.uy.core.entity.CincoDeOro;
import banca.uy.core.resources.dto.Representacion;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.ICincoDeOroService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/cincoDeOro")
public class CincoDeOroController {

  Logger logger = LogManager.getLogger(CincoDeOroController.class);

  @Autowired
  ICincoDeOroService cincoDeOroService;

  private final IAuthenticationFacade authenticationFacade;

  public CincoDeOroController(IAuthenticationFacade authenticationFacade) {
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping("/inicializarBaseDeDatos")
  public ResponseEntity inicializarBaseDeDatos(@RequestBody String tirada) {
    try {
      cincoDeOroService.inicializarBaseDeDatos(tirada);
      return ok("terminamos de actualizar la base de datos " + tirada);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/actualizarBaseDeDatos")
  public ResponseEntity actualizarBaseDeDatos() {
    try {
      cincoDeOroService.actualizarBaseDeDatos();
      return ok("terminamos de actualizar la base de datos");
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerUltimaJugada")
  public Representacion<CincoDeOro> obtenerUltimaJugada() {
    try {
      CincoDeOro cincoDeOro = cincoDeOroService.obtenerUltimaJugada();
      return new Representacion<>(HttpStatus.OK.value(), cincoDeOro);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/obtenerJugadasAnteriores")
  public Representacion<List<CincoDeOro>> obtenerJugadasAnteriores(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
          @RequestBody CincoDeOro cincoDeOro
  ) {
    try {
      List<CincoDeOro> ultimasJugadas = cincoDeOroService.obtenerJugadasAnteriores(cincoDeOro, page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @PostMapping("/obtenerJugadasPosteriores")
  public Representacion<List<CincoDeOro>> obtenerJugadasPosteriores(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
          @RequestBody CincoDeOro cincoDeOro
  ) {
    try {
      List<CincoDeOro> ultimasJugadas = cincoDeOroService.obtenerJugadasPosteriores(cincoDeOro, page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerUltimasJugadas")
  public Representacion<List<CincoDeOro>> obtenerUltimasJugadas(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size
  ) {
    try {
      List<CincoDeOro> ultimasJugadas = cincoDeOroService.obtenerUltimasJugadas(page, size);
      return new Representacion<>(HttpStatus.OK.value(), ultimasJugadas);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias")
  public Representacion<HashMap<Integer, List<CincoDeOro>>> obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias(
          @RequestParam(defaultValue = "1") int numeroDeCoincidencias
  ) {
    try {
      HashMap<Integer, List<CincoDeOro>> jugadasCincoDeOroConMayorNumeroDeCoincidencias = cincoDeOroService.obtenerJugadasCincoDeOroConMayorNumeroDeCoincidencias(numeroDeCoincidencias);
      return new Representacion<>(HttpStatus.OK.value(), jugadasCincoDeOroConMayorNumeroDeCoincidencias);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/jugadasCincoDeOroNumeroDeCoincidencias")
  public Representacion<HashMap<Integer, List<CincoDeOro>>> jugadasCincoDeOroNumeroDeCoincidencias(
          @RequestParam(defaultValue = "1") int numeroDeCoincidencias,
          @RequestBody CincoDeOro cincoDeOro
  ) {
    try {
      HashMap<Integer, List<CincoDeOro>> jugadasCincoDeOroNumeroDeCoincidencias = cincoDeOroService.obtenerJugadasCincoDeOroNumeroDeCoincidencias(numeroDeCoincidencias, cincoDeOro);
      return new Representacion<>(HttpStatus.OK.value(), jugadasCincoDeOroNumeroDeCoincidencias);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  @GetMapping("/obtenerTodasLasCombinaciones")
  public Representacion<List<List<Integer>>> obtenerTodasLasCombinaciones(
  ) {
    try {
      List<List<Integer>> permutaciones = cincoDeOroService.obtenerTodasLasCombinaciones();
      return new Representacion<>(HttpStatus.OK.value(), permutaciones);
    } catch (Exception ex) {
      logger.log(Level.ERROR, "precios controller @PostMapping(\"/excel/actualizar\") Error:", ex.getMessage(), ex.getStackTrace());
      throw new WebApplicationException("Ocurrió un error al actualizar los productos - " + ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}