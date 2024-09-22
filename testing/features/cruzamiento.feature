# language: es

Característica: Cruzamiento de registros de agentes de transito y registros de conductores

Esquema del escenario: Registro de conductor sin registro de agente de transito
      Dado que se recibe el registro de conductor con "<horaInicio>" y "<horaFin>" y "<patente>"
      Cuando se lleva a cabo el proceso de cruzamiento de registros
      Entonces no se genera ninguna infraccion

      Ejemplos:
      | horaInicio                 | horaFin                 | patente |
      | 2024-09-20 16:00:00.000    | 2024-09-20 17:00:00.000 | ABC 123 |