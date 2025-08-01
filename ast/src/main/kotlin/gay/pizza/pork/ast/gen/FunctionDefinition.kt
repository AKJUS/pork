// GENERATED CODE FROM PORK AST CODEGEN
package gay.pizza.pork.ast.gen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("functionDefinition")
class FunctionDefinition(override val modifiers: DefinitionModifiers, override val symbol: Symbol, val arguments: List<ArgumentSpec>, val returnType: TypeSpec?, val block: Block?, val nativeFunctionDescriptor: NativeFunctionDescriptor?) : Definition() {
  override val type: NodeType = NodeType.FunctionDefinition

  override fun <T> visitChildren(visitor: NodeVisitor<T>): List<T> =
    visitor.visitAll(listOf(symbol), arguments, listOf(returnType), listOf(block), listOf(nativeFunctionDescriptor))

  override fun <T> visit(visitor: NodeVisitor<T>): T =
    visitor.visitFunctionDefinition(this)

  override fun equals(other: Any?): Boolean {
    if (other !is FunctionDefinition) return false
    return other.modifiers == modifiers && other.symbol == symbol && other.arguments == arguments && other.returnType == returnType && other.block == block && other.nativeFunctionDescriptor == nativeFunctionDescriptor
  }

  override fun hashCode(): Int {
    var result = modifiers.hashCode()
    result = 31 * result + symbol.hashCode()
    result = 31 * result + arguments.hashCode()
    result = 31 * result + returnType.hashCode()
    result = 31 * result + block.hashCode()
    result = 31 * result + nativeFunctionDescriptor.hashCode()
    result = 31 * result + type.hashCode()
    return result
  }
}
