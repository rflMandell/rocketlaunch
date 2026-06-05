package br.com.fiap.rocketlaunch.service.validation;

import br.com.fiap.rocketlaunch.domain.entity.Launch;

public interface LaunchValidator {
    void validate(Launch launch);
}