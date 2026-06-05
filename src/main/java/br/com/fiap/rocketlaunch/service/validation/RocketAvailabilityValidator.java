package br.com.fiap.rocketlaunch.service.validation;

import br.com.fiap.rocketlaunch.domain.entity.Launch;
import br.com.fiap.rocketlaunch.domain.enums.RocketStatus;
import br.com.fiap.rocketlaunch.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class RocketAvailabilityValidator implements LaunchValidator {

    @Override
    public void validate(Launch launch) {
        if (launch.getRocket() == null) {
            throw new BusinessException("Lançamento deve ter um foguete associado.");
        }
        if (launch.getRocket().getStatus() == RocketStatus.MAINTENANCE) {
            throw new BusinessException(
                    "Foguete '" + launch.getRocket().getName() +
                            "' está em manutenção e não pode ser utilizado."
            );
        }
        if (launch.getRocket().getStatus() == RocketStatus.DECOMMISSIONED) {
            throw new BusinessException(
                    "Foguete '" + launch.getRocket().getName() +
                            "' foi descomissionado e não pode ser utilizado."
            );
        }
    }
}