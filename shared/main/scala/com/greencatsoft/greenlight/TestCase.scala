package com.greencatsoft.greenlight

import com.greencatsoft.greenlight.grammar.Statement.CaseDefinition

case class TestCase[A](definition: CaseDefinition[A], content: () => Unit)
