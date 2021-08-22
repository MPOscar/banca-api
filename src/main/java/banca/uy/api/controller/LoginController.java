package banca.uy.api.controller;

import javax.transaction.Transactional;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;

import banca.uy.core.resources.dto.Representacion;
import banca.uy.core.resources.dto.UsuarioBasic;
import banca.uy.core.security.IAuthenticationFacade;
import banca.uy.core.services.interfaces.IErrorService;
import banca.uy.core.services.interfaces.ILoginService;
import banca.uy.core.resources.dto.LoginResponse;
import banca.uy.core.resources.dto.UsuarioPrincipal;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import banca.uy.core.exceptions.ServiceException;

@RestController
@RequestMapping("/auth")
public class LoginController {
	@SuppressWarnings("unused")
	Logger logger = LogManager.getLogger(LoginController.class);

	private final IAuthenticationFacade authenticationFacade;
	private ILoginService loginService;
	private IErrorService errorService;

	public LoginController(ILoginService loginService, IErrorService errorService, IAuthenticationFacade authenticationFacade) {
		this.loginService = loginService;
		this.authenticationFacade = authenticationFacade;
		this.errorService = errorService;
	}

	@PostMapping("/login")
	@Transactional
	public Representacion<LoginResponse> authenticateUser(@RequestBody UsuarioBasic usuario) {
		try {
			LoginResponse response = loginService.LoginBasic(usuario);
			this.errorService.Log("login controller post: /login Exitoso: " + usuario.getUsuario());
			this.errorService
					.Log("login controller @PostMapping(\"/login\")  Error: " , " StackTrace: ");
			return new Representacion<LoginResponse>(HttpStatus.OK.value(), response);
		} catch (ServiceException ex) {
			logger.log(Level.ERROR, "login controller Error:", ex.getMessage(), ex.getStackTrace());
			this.errorService
					.Log("login controller @PostMapping(\"/login\")  Error: " + ex.getMessage() , " StackTrace: " + ex.getStackTrace());
			throw new WebApplicationException(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
		} catch (Exception ex) {
			logger.log(Level.ERROR, "login controller Error:", ex.getMessage(), ex.getStackTrace());
			throw new WebApplicationException("Ocurró un error inesperado, intente nuevamente - " + ex.getMessage(),
					HttpStatus.UNAUTHORIZED.value());
		}
	}

}
