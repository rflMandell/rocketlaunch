package br.com.fiap.rocketlaunch;

import org.springframework.boot.SpringApplication;

public class TestRocketLaunchSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(RocketLaunchSimulatorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
