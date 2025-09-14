package mx.santander.h2h.monitoreoapi.model.entity;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**

 * Bean para poder obtener la informacion de conexion a base de datos desde el

 * archivo de properties o yml

 *

 * @author Felipe Cazarez - Adaptado por Rodrigo RPM

 * @since 09/12/22

 */

@Getter

@Setter

@NoArgsConstructor

@Configuration

@EnableConfigurationProperties

@ConfigurationProperties(prefix = "h2h.datasource")

public class DBH2HConfigurationBean implements Serializable {

    /**

     * id serial

     */

    private static final long serialVersionUID = -3191615954848875075L;

    /**

     * campo para pder obtener el driver de la configuracion para acceso a base de

     * datos

     */

    private String driver;

    /**

     * campo para poder obtener el tiempo maximo de conexion a la bd

     */

    private String connectiontime;

    /**

     * campo para poder obtener la contraseña para el acceso a la bd

     */

    private String password;

    /**

     * campo para poder obtener el tamaño del pool de conexiones de la bd.

     */

    private String poolsize;

    /**

     * Campo para poder obtener el valor de la url para la conexion a bd

     */

    private String url;

    /**

     * campo para poder obtener el nombre de usuario para el acceso a la bd

     */

    private String username;

}
