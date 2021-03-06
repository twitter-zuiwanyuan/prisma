package com.prisma.deploy.connector.mysql.database

import com.prisma.shared.models.Field
import slick.jdbc.SQLActionBuilder
import slick.jdbc.MySQLProfile.api._

object DatabaseQueryBuilder {

  def existsByModel(projectId: String, modelName: String): SQLActionBuilder = {
    sql"select exists (select `id` from `#$projectId`.`#$modelName`)"
  }

  def existsByRelation(projectId: String, relationId: String): SQLActionBuilder = {
    sql"select exists (select `id` from `#$projectId`.`#$relationId`)"
  }

  def existsNullByModelAndScalarField(projectId: String, modelName: String, fieldName: String) = {
    sql"""SELECT EXISTS(Select `id` FROM `#$projectId`.`#$modelName`
          WHERE `#$projectId`.`#$modelName`.#$fieldName IS NULL)"""
  }

  def existsNullByModelAndRelationField(projectId: String, modelName: String, field: Field) = {
    val relationId   = field.relation.get.relationTableName
    val relationSide = field.relationSide.get.toString
    sql"""select EXISTS (
            select `id`from `#$projectId`.`#$modelName`
            where `id` Not IN
            (Select `#$projectId`.`#$relationId`.#$relationSide from `#$projectId`.`#$relationId`)
          )"""
  }
}
