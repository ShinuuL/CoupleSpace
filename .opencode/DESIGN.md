# CoupleSpace — Memórias sob o Céu Galáctico 🌌
## Documento de Design e Identidade Visual (DESIGN.MD)

Este documento descreve as decisões de design, a arquitetura do software e a identidade visual que formam a alma de **CoupleSpace** (Starry Space for Couples), uma aplicação móvel intimista e romântica desenvolvida nativamente com **Jetpack Compose**, **Kotlin** e **Room Database**.

---

## 1. Visão do Projeto & Direção Criativa

O **CoupleSpace** foi concebido não apenas como uma ferramenta utilitária, mas como um **santuário digital seguro e poético para casais**. A filosofia do design prioriza o afeto, a intimidade e a imersão emocional sob a metáfora de uma viagem estelar conjunta.

### O Tema: *CoupleSpace Estelar* (Starry CoupleSpace Theme)
O tema do aplicativo une o mistério poético do espaço sideral com o aconchego de uma cabana sob as estrelas. O visual afasta-se de interfaces corporativas cinzas e sem vida ("AI slop") para abraçar uma estética **Cyberpunk-Romantic de Luxo Escuro**, utilizando:
*   **Profundidade & Transparência**: Cartões glassmórficos com opacidade reduzida e bordas iluminadas por gradientes lineares.
*   **Dinamicidade Sutil**: Um fundo estelar infinito que brilha de forma assíncrona, reagindo ao tempo.
*   **Páginas Orgânicas**: Polaroids inclinadas aleatoriamente, emulando um álbum de recortes analógico real (scrapbook).

---

## 2. Paleta de Cores & Elementos de Design

A identidade de cores reflete a escuridão do espaço profundo pontilhada por estrelas neon brilhantes e cores quentes e românticas:

| Token de Cor | Cor Hex | Uso Visual |
| :--- | :--- | :--- |
| **Deep Nebula (Fundo)** | `#10131F` | O preto-azul profundo que serve de espaço sideral estável para os olhos. |
| **Space Black (Cartão)** | `#141828` | Superfície glassmórfica dos cartões com opacidade de 75% para profundidade extra. |
| **Neon Cyan (Destaque)** | `#00F0FF` | Energia estelar, ícones de foco, títulos galácticos, botões de ação e estrelas cintilantes. |
| **Glowing Lavender** | `#CABEFF` | Tonalidade lilás mística para bordas de luz, subtítulos e textos auxiliares. |
| **Pastel Rose** | `#FFFFCBE6` | O toque afetivo para marcos de jantar, reações românticas e corações. |
| **Starry White** | `#FFFFFF` | Branco puro de alto contraste para leitura perfeita de mensagens e notas. |

---

## 3. Arquitetura de Software & Fluxo de Dados

O aplicativo segue o padrão arquitetural **MVVM (Model-View-ViewModel)** com **Clean Architecture**, garantindo reatividade contínua e persistência offline-first confiável.

```
       [ Jetpack Compose UI Screens ] 
                     ▲
                     │ (StateFlow / CollectAsStateWithLifecycle)
                     ▼
            [ CoupleSpaceViewModel ]
                     ▲
                     │ (Coroutines / Flows)
                     ▼
           [ CoupleSpaceRepository ]
                     ▲
                     │
                     ▼
           [ Room DAO Interfaces ] ◄──► [ SQLite local database ]
```

### Componentes Técnicos Chave:
1.  **Room Database (`CoupleSpaceDatabase`)**:
    *   **`DiaryNote`**: Guarda as notas doces do diário, incluindo links de imagens galácticas predefinidas e um ângulo de rotação (`tiltAngle`) gerado de forma pseudo-aleatória estável para criar o scrapbook assimétrico.
    *   **`CalendarEvent`**: Registra as datas especiais (aniversários, viagens, jantares à luz de velas).
    *   **`ChatMessage`**: Persiste as conversas, com suporte a imagens de flores prensadas enviadas e estados de reação.
    *   **`MoodLog`**: Monitora a flutuação do humor diário da exploradora com registros de data e sentimentos.
2.  **CoupleSpaceViewModel**:
    *   Expõe estados imutáveis usando `StateFlow` e lida com operações assíncronas através do escopo do ciclo de vida da UI (`viewModelScope`), mantendo as consultas eficientes e fora da thread principal do Android.
3.  **Jetpack Navigation**:
    *   Navegação por grafo limpa, segura e com transições suaves que gerenciam a entrada do portal de login até as visualizações centrais.

---

## 4. Engenharia Visual & Destaques de UI

### A. O Motor de Cintilação Procedural (`StardustBackground`)
Em vez de utilizar uma imagem estática pesada ou um loop de vídeo que consumiria muita bateria e memória, o CoupleSpace renderiza um **fundo estelar procedimental nativo em tempo de execução**:
*   Utiliza `Modifier.drawBehind` que opera diretamente na camada de desenho da GPU.
*   Gera mais de 80 estrelas vetorizadas com posições estabilizadas por uma semente geradora estável.
*   Aplica uma animação infinita de pulsação de opacidade (`infiniteRepeatable` com `alpha1` e `alpha2`) intercalando frequências e atrasos de transição diferentes para que as estrelas pisquem organicamente de forma assíncrona.

```kotlin
val infiniteTransition = rememberInfiniteTransition(label = "stars")
val alpha1 by infiniteTransition.animateFloat(
    initialValue = 0.2f, targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
        animation = tween(1500, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
    )
)
```

### B. O Álbum de Recortes Assimétrico (Home Bento Grid)
A tela inicial apresenta os dados como um mural físico:
*   **Polaroid Dinâmicas**: Cada item carregado do banco de dados ganha um ângulo de inclinação único no plano bidimensional (`note.tiltAngle`), quebrado propositalmente da grade reta para dar a sensação de fotos coladas manualmente com fita.
*   **Player de Música Integrado ("Nossa Música")**: Cartão compacto reproduzindo um mockup visual do soundscape de casal do outono com controles interativos de reprodução e animação de rotação sutil.
*   **Seletor de Imagens Galácticas**: No modal de inserção de memórias doces, a usuária pode escolher entre fotos afetivas de alta qualidade pré-processadas no Google Photos (Café aconchegante, Flores, Velas de jantar, Sunset estelar).

### C. Chat dos Namorados com Reação de Estrelas Física
A tela de chat de mensagens foi projetada para interações mágicas:
*   **Double-Tap Reaction**: Ao dar um duplo clique em qualquer mensagem recebida ou enviada, uma animação fluida de **Estrela Galáctica** de amor surge da tela usando transições de escala elásticas (`spring` com amortecimento médio bouncy) e esmaece suavemente.
*   **Flores Prensadas Virtuais**: Permite visualizar fotos com filtros emoldurados e cartas de afeto.

---

## 5. Como o Outro Projeto Foi Incluído Como Tema

A inclusão do projeto original de design da web corporado com os recursos CSS estilizados de galáxias luminosas foi traduzida pixel a pixel para o Android:
1.  **Imagens Hospedadas no Google**: As mesmas URLs públicas de imagens usadas nas maquetes originais foram portadas para o carregamento assíncrono via biblioteca **Coil** em Kotlin.
2.  **Glossmorfismo Dinâmico**: A translucidez do vidro do design web foi replicada aplicando efeitos de `Box` com cor de preenchimento `Color(0xFF141828).copy(alpha = 0.75f)` combinadas com bordas finas com pincel gradiente de luz (`Brush.linearGradient`).
3.  **Ícones Modernos**: Uso de ícones circulares com bordas iluminadas e sinalizadores dinâmicos piscantes na barra lateral para emular a sensação de portal web estelar ativo.

---
*CoupleSpace Estelar — Desenvolvido com amor, lógica e poética visual.*
