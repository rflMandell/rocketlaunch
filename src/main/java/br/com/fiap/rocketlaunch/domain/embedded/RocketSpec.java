package br.com.fiap.rocketlaunch.domain.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RocketSpec {

    @NotNull
    @Positive
    private Double thrustKN;           // empuxo em kilonewtons

    @NotNull
    @Positive
    private Double maxPayloadKg;       // capacidade max de carga em kg

    @NotNull
    @Positive
    private Double heightMeters;       // altura do foguete em metros

    @NotNull
    @Positive
    private Integer stages;            // número de estagios do foguete

    @NotNull
    @Positive
    private Double fuelCapacityLiters; // capacidade de combustível em litros
}