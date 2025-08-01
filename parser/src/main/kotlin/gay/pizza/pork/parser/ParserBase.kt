package gay.pizza.pork.parser

import gay.pizza.pork.ast.gen.Node
import gay.pizza.pork.ast.gen.NodeParser
import gay.pizza.pork.ast.gen.NodeType
import gay.pizza.pork.tokenizer.*

abstract class ParserBase(source: TokenSource, val attribution: NodeAttribution) : NodeParser {
  val source: TokenSource = source as? ParserAwareTokenSource ?: LazySkippingTokenSource(source, TokenType.ParserIgnoredTypes)

  @Suppress("NOTHING_TO_INLINE")
  protected inline fun <T: Node> produce(type: NodeType, noinline block: () -> T): T =
    attribution.produce(type, block)

  @Suppress("NOTHING_TO_INLINE")
  protected inline fun <T: Node> expect(type: NodeType, vararg tokenTypes: TokenType, noinline block: (Token) -> T): T =
    produce(type) {
      block(expect(*tokenTypes))
    }

  protected fun <T> collect(
    peeking: TokenType,
    consuming: TokenType? = null,
    read: () -> T
  ): List<T> {
    val items = mutableListOf<T>()
    while (!peek(peeking)) {
      val item = read()
      if (consuming != null) {
        if (!peek(peeking)) {
          expect(consuming)
        }
      }
      items.add(item)
    }
    return items
  }

  protected fun <T> oneAndContinuedBy(separator: TokenType, read: () -> T): List<T> {
    val items = mutableListOf<T>()
    items.add(read())
    while (peek(separator)) {
      expect(separator)
      items.add(read())
    }
    return items
  }

  protected fun peek(vararg types: TokenType): Boolean {
    val token = peek()
    return types.contains(token.type)
  }

  protected fun next(type: TokenType): Boolean {
    return if (peek(type)) {
      expect(type)
      true
    } else false
  }

  protected fun expect(vararg types: TokenType): Token {
    val token = next()
    if (!types.contains(token.type)) {
      expectedTokenError(token, *types)
    }
    return token
  }

  protected fun <T: Node> expect(vararg types: TokenType, consume: (Token) -> T): T =
    consume(expect(*types))

  protected fun expectedTokenError(token: Token, vararg types: TokenType): Nothing {
    throw ExpectedTokenError(token, token.sourceIndex, *types)
  }

  protected fun next(): Token {
    val token = source.next()
    attribution.push(token)
    return token
  }
  protected fun peek(): Token = source.peek()
  protected fun peek(ahead: Int): TokenType = source.peekTypeAhead(ahead)
  protected fun peek(ahead: Int, vararg types: TokenType): Boolean = types.contains(source.peekTypeAhead(ahead))
}
