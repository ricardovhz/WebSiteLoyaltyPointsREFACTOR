# language: pt
Funcionalidade: Enrollment (inscrição)

  Um estudante que quer participar de um curso pode reservar uma vaga.
  A vaga fica reservada ate a confirmação do pagamento.

  Rule: Estudantes podem se inscrever quando tiver vagas

  Cenário: Estudante reserva uma vaga em um curso com vagas em aberto

    Dado um estudante que quer participar de um curso
    E o curso tem ainda 5 vagas em aberto
    Quando o estudante reserva sua vaga
    Então a vaga deveria estar marcada para esperando pagamento
    E o curso deveria ter somente 4 vagas em aberto

  Cenário: Estudante nao reserva uma vaga em um curso sem vagas em aberto

    Dado um estudante que quer participar de um curso
    E o curso nao tem vagas em aberto
    Quando o estudante reserva sua vaga
    Então o estudante nao deve estar reservado
