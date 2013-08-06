package com.azaptree.elasticsearch.componentLifeCycles

import com.azaptree.application.component.ComponentLifeCycle
import org.elasticsearch.node.Node
import com.azaptree.application.component.ComponentStopped
import com.azaptree.application.component.ComponentStarted
import com.azaptree.application.component.ComponentInitialized
import com.azaptree.application.component.Component
import com.azaptree.application.component.ComponentConstructed
import com.azaptree.application.component.ComponentNotConstructed
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.common.settings.Settings

case class ElasticNodeLifeCycle(
    clusterName: Option[String] = None,
    loadConfigSettings: Option[Boolean] = None,
    client: Option[Boolean] = None,
    data: Option[Boolean] = None,
    settings: Option[Settings] = None) extends ComponentLifeCycle[Node] {

  override protected def create(comp: Component[ComponentNotConstructed, Node]): Component[ComponentConstructed, Node] = {
    val nb = NodeBuilder.nodeBuilder()
    clusterName.foreach(nb.clusterName(_))
    loadConfigSettings.foreach(nb.loadConfigSettings(_))
    client.foreach(nb.client(_))
    data.foreach(nb.data(_))
    settings.foreach(nb.settings(_))
    comp.copy[ComponentConstructed, Node](componentObject = Some(nb.build()))
  }

  override protected def start(comp: Component[ComponentInitialized, Node]): Component[ComponentStarted, Node] = {
    val startedNode = for {
      node <- comp.componentObject
    } yield {
      node.start()
    }

    comp.copy[ComponentStarted, Node](componentObject = startedNode)
  }

  override protected def stop(comp: Component[ComponentStarted, Node]): Component[ComponentStopped, Node] = {
    comp.componentObject.foreach(_.close())
    comp.copy[ComponentStopped, Node](componentObject = None)
  }

}