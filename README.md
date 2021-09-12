# Java Encoder Decoder
Simples programa que dispõe de vários métodos de codificação e decodificação, além de rotinas para o tratamento de erro usando Hamming e CRC.

![Janela principal do programa](https://i.imgur.com/uQEr9PF.png)

O programa é capaz de manipular um arquivo e operar nas seguintes funcionalidades:
**Codificação:** Arquivo.txt -> Arquivo.enc
**Decodificação:** Arquivo.enc -> Arquivo.dec
**Tratamento de erro:** Arquivo.enc -> Arquivo.enc.ecc -> Arquivo.enc

## Implementando um enconder
A classe `Encoder` possui dois contratos responsáveis pela codificação e decodificação de uma entrada de dados.

Tanto o método de codificação quanto o de decodificação recebem dois argumentos, sendo o primeiro a stream de dados do arquivo original e o segundo a stream de saída do arquivo processado.
```java
public interface Encoder {
    public void encode(InputStream reader, OutputStream writer) throws IOException;   
    public void decode(InputStream reader, OutputStream writer) throws IOException;
}
```
## Leitura e escrita bit-a-bit
Para facilitar a leitura e geração dos codewords são disponibilizadas duas classes para a manipulação de streams de dados a nivel de bit.

A classe `InputBitStream` permite a leitura de uma quantidade específica de bits, facilitando o parsing do arquivo codificado.
```java
// Decodificacao unaria
InputBitStream bstream = new InputBitStream(reader);
while(bstream.hasNext()) {
    int symbol = (int) bstream.countWhile(false);
    if (!bstream.isNextBitSet()) break;
    bstream.nextBit(); // Stop bit
    writer.write(symbol);
}
```
A classe `OutputBitStream` permite a escrita de uma quantidade específica de bits, facilitando a escrita dos codewords gerados pelo algoritmo de compressão.
*OBS.: Internamente, a classe usa um buffer do tamanho de um byte que é escrito no output assim que estiver cheio, por isso é recomendável chamar o método `flush` ao final do processamento para garantir assim, a escrita dos bits remanescentes no buffer.*
```java
// Codificacao unaria
OutputBitStream bstream = new OutputBitStream(writer);
int symbol = reader.read();
while (symbol != -1) {
    bstream.writeBits(false, symbol);
    bstream.writeBit(true); // Stop bit
    symbol = reader.read();
}
bstream.flush(); // Esvazia o buffer
```
## Codificações suportadas
As seguintes codificações foram implementadas  e estão disponíveis no programa:

 - [x] Delta
 - [x] Elias-Gamma
 - [x] Fibonacci
 - [x] Golomb
 - [x] Unária

## Tratamento de erros
![Opções para tratamento de erro](https://i.imgur.com/CBucu5Z.png)

O programa possui a função de gerar um arquivo `.ecc` para o tratamento de erros. Esse arquivo e codificado utilizando **Hamming** permitindo corrigir automaticamente os bits inconsistentes durante a decodificação.

Só é possível gerar arquivos de tratamento de erros para arquivos que foram codificados pelo programa (`.enc`), uma vez que o cabeçalho do arquivo codificado é preservado no arquivo de tratamento de erros. Esse cabeçalho é processado por uma divisão polinomial **CRC** que gera um byte de verificação, o qual é concatenado logo após o cabeçalho.

### Log de inconsistências
Qualquer inconsistência detectada durante o processo de tratamento de erros será registrado em um arquivo de log armazenado na pasta do executável.
