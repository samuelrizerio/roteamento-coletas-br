#!/bin/bash

# ========================================
# SCRIPT PARA GERAR CHAVE JWT SEGURA
# ========================================
# 
# Este script gera uma chave JWT segura para uso em produção
# 
# USO:
#   ./scripts/generate-jwt-secret.sh
#   ./scripts/generate-jwt-secret.sh 64  # Para chave de 64 caracteres

echo "🔐 GERANDO CHAVE JWT SEGURA..."
echo ""

# Verificar se o OpenSSL está disponível
if ! command -v openssl &> /dev/null; then
    echo "❌ ERRO: OpenSSL não encontrado!"
    echo "💡 Instale o OpenSSL: sudo apt install openssl"
    exit 1
fi

# Tamanho da chave (padrão: 32 bytes = 64 caracteres hex)
KEY_SIZE=${1:-32}

echo "📏 Tamanho da chave: ${KEY_SIZE} bytes (${KEY_SIZE}*2 caracteres hex)"
echo ""

# Gerar chave aleatória
JWT_SECRET=$(openssl rand -hex ${KEY_SIZE})

echo "✅ CHAVE JWT GERADA COM SUCESSO!"
echo ""
echo "🔑 Chave JWT:"
echo "   ${JWT_SECRET}"
echo ""
echo "📋 Para usar em produção:"
echo "   export JWT_SECRET=\"${JWT_SECRET}\""
echo ""
echo "📋 Para Docker:"
echo "   -e JWT_SECRET=\"${JWT_SECRET}\""
echo ""
echo "📋 Para arquivo .env:"
echo "   JWT_SECRET=${JWT_SECRET}"
echo ""
echo "⚠️  IMPORTANTE:"
echo "   - Guarde esta chave em local seguro"
echo "   - NUNCA commite no repositório"
echo "   - Use HTTPS em produção"
echo "   - Rotacione a chave regularmente"
echo ""
echo "🔒 Segurança da chave:"
echo "   - Entropia: ${KEY_SIZE} bytes"
echo "   - Caracteres possíveis: 0-9, a-f"
echo "   - Combinações: 16^${KEY_SIZE} (${KEY_SIZE}*4 bits)"
echo ""
echo "🎯 Recomendações:"
echo "   - Use chaves de pelo menos 32 bytes (64 caracteres)"
echo "   - Em produção, use 64 bytes (128 caracteres)"
echo "   - Rotacione a cada 90 dias"
echo "   - Use variáveis de ambiente"
echo "   - Monitore logs de acesso"
