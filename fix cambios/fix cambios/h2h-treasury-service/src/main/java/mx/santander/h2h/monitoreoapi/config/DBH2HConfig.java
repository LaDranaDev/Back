package mx.santander.h2h.monitoreoapi.config;

import com.zaxxer.hikari.HikariConfig;

import com.zaxxer.hikari.HikariDataSource;

import mx.santander.h2h.monitoreoapi.model.entity.DBH2HConfigurationBean;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.orm.jpa.JpaTransactionManager;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

/**

 * Clase para poder realizar la

 * configuracion a la base de datos

 * poder crear el manejador de

 * transacciones y especificar de donde

 * tomara los repositorios

 *

 * @author fcazarez - Adaptado por Rodrigo RPM

 * @since 09/12/2022

 *

 */

@Configuration

@EnableTransactionManagement

@EnableJpaRepositories(

        entityManagerFactoryRef = "h2hEntityManagerFactory",

        transactionManagerRef = "h2hTransactionManager",

        basePackages = {"mx.santander.h2h.monitoreoapi.repository"}

)

public class DBH2HConfig {

    /**

     * Descripcion : Bean de configuracion de hikari,

     * para el pool de conexiones, define la base de datos,

     * las credenciales, el tamanio maximo de conexiones,

     * el tamanio minimo y el tiempo de espera.

     *

     * @author Felipe Cazarez

     * @since  09/12/2022

     *

     * @param h2hConfig contiene el bean con las propiedades

     * de configuracion

     *

     * @return Devuelve la propiedad del tipo HikariConfig

     */

    @Bean

    @Primary

        public HikariConfig hikariConfig(DBH2HConfigurationBean h2hConfig) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(h2hConfig.getUrl());

        config.setDriverClassName(h2hConfig.getDriver());

        config.setPassword(h2hConfig.getPassword());

        config.setUsername(h2hConfig.getUsername());

        config.setMaximumPoolSize(Integer.parseInt(h2hConfig.getPoolsize()));

        config.setConnectionTimeout(Integer.parseInt(h2hConfig.getConnectiontime()));

        config.setAutoCommit(false);

        return config;

    }

    /**

     * Descripcion : Bean de fuente de datos,

     * de este bean podemos crear las conexiones

     * que son manejadas por el pool,

     * para hacer el acceso a datos.

     *

     * @author Felipe Cazarez

     * @since  09/12/2022

     *

     * @param h2hConfig contiene el bean con las propiedades

     * de configuracion

     *

     * @return Devuelve la propiedad del tipo DataSource

     */

    @Bean("dataSourceH2H")

    @Primary

    public DataSource dataSource(DBH2HConfigurationBean h2hConfig) {

        return new HikariDataSource(hikariConfig(h2hConfig));

    }

    /**

     * Descripcion : Metodo de construccion

     * del entitymanager de la base

     * de datos principal

     *

     * @author Felipe Cazarez

     * @since  09/12/2022

     *

     * @param builder de tipo EntityManagerFactoryBuilder

     * @param dataSource de tipo DataSource

     *

     * @return Devuelve la propiedad del tipo LocalContainerEntityManagerFactoryBean

     */

    @Bean(name = "h2hEntityManagerFactory")

    @Primary

    public LocalContainerEntityManagerFactoryBean h2hEntityManagerFactory(EntityManagerFactoryBuilder builder,

                                                                          @Qualifier("dataSourceH2H") DataSource dataSource) {

        return builder.dataSource(dataSource)

                .packages("mx.santander.h2h.monitoreoapi.model.entity")

                .persistenceUnit("DS-h2h")

                .build();

    }

    /**

     * Descripcion : Metodo para la construccion del

     * transaction manager de la base de datos principal

     *

     * @author Felipe Cazarez

     * @since  09/12/2022

     *

     * @param h2hEntityManagerFactory de tipo EntityManagerFactory

     *

     * @return Devuelve la propiedad del tipo PlatformTransactionManager

     */

    @Bean(name = "h2hTransactionManager")

    @Primary

    public PlatformTransactionManager h2hTransactionManager(@Qualifier("h2hEntityManagerFactory") EntityManagerFactory h2hEntityManagerFactory) {

        return new JpaTransactionManager(h2hEntityManagerFactory);

    }

    /**

     * Descripcion : Metodo para la generacion

     * de un Jdbctemplate, para utlilizar Spring JDBC

     *  para las consultas a BD

     *

     * @author Felipe Cazarez

     * @since  09/12/2022

     *

     * @param dataSourceH2H el data

     * source de la base de datos de h2h.

     *

     * @return Devuelve la propiedad del tipo JdbcTemplate

     */

    @Bean(name = "h2hJdbcTemplate")

    @Primary

    public JdbcTemplate h2hJdbcTemplate(@Qualifier("dataSourceH2H") DataSource dataSourceH2H) {

        return new JdbcTemplate(dataSourceH2H);

    }

}

